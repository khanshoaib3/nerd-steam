package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.model.api.AppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.remote.SteamInternalWebApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

private const val TAG = "GameDetailRepository"

interface SteamRepository {
    suspend fun fetchDataForAppId(appId: Int): AppDetailsResponse?
}

class OnlineSteamRepository @Inject constructor(
    private val steamInternalWebApiService: SteamInternalWebApiService,
) : SteamRepository {
    // TODO Use Result here
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetchDataForAppId(appId: Int): AppDetailsResponse? {
        var result = steamInternalWebApiService.getAppDetails(appId)
        val json = Json { ignoreUnknownKeys = true }
        Log.d("GameDetailRepository", "$result")

        lateinit var appDetail: AppDetailsResponse
        var missingField = false
        try {
            appDetail =
                json.decodeFromJsonElement<AppDetailsResponse>(result.get(key = "$appId")!!)
        } catch (_: MissingFieldException) {
            Log.d(TAG, "Unable to serialize data for $appId because of missing fields.")
            Log.d(TAG, "Attempting to fetch updated App ID from redirect url...")
            missingField = true
        }

        if (!missingField && appDetail.success) return appDetail

        val newAppId = getUpdatedAppIdFromRedirectUrl(appId)
        if (newAppId == null) return null
        Log.d(TAG, "New app id: $newAppId")

        result = steamInternalWebApiService.getAppDetails(newAppId)
        return json.decodeFromJsonElement<AppDetailsResponse>(result.get(key = "$newAppId")!!)
    }

    private fun getUpdatedAppIdFromRedirectUrl(currentAppId: Int): Int? {
        val regex =
            Regex("""https://store\.steampowered\.com/app/([0-9]+)""", RegexOption.MULTILINE)
        val url = getRedirectUrl("https://store.steampowered.com/app/$currentAppId") ?: return null

        val match = regex.find(url)
        return match?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun getRedirectUrl(url: String): String? {
        lateinit var urlTmp: URL
        lateinit var redUrl: String
        lateinit var connection: HttpURLConnection

        try {
            urlTmp = URL(url)
        } catch (e: MalformedURLException) {
            Log.e(TAG, "MalformedURLException occurred when accessing url: $url")
            Log.e(TAG, "${e.localizedMessage}")
            return null
        }

        try {
            connection = urlTmp.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            Log.e(TAG, "IOException occurred when accessing url: $url")
            Log.e(TAG, "${e.localizedMessage}")
            return null
        }

        try {
            connection.getResponseCode()
        } catch (e: IOException) {
            Log.e(TAG, "IOException occurred when accessing url: $url")
            Log.e(TAG, "${e.localizedMessage}")
            return null
        }

        redUrl = connection.getURL().toString()
        connection.disconnect()

        return redUrl
    }
}