package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.model.detail.SteamWebApiAppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.remote.SteamInternalWebApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

private const val TAG = "GameDetailRepository"

interface GameDetailRepository {
    suspend fun fetchDataForAppId(appId: Int): SteamWebApiAppDetailsResponse?
}

class OnlineGameDetailRepository @Inject constructor(
    private val steamInternalWebApiService: SteamInternalWebApiService,
) : GameDetailRepository {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetchDataForAppId(appId: Int): SteamWebApiAppDetailsResponse? {
        var result = steamInternalWebApiService.getAppDetails(appId)
        val json = Json { ignoreUnknownKeys = true }
        Log.d("GameDetailRepository", "$result")

        lateinit var appDetail: SteamWebApiAppDetailsResponse
        var missingField = false
        try {
            appDetail =
                json.decodeFromJsonElement<SteamWebApiAppDetailsResponse>(result.get(key = "$appId")!!)
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
        return json.decodeFromJsonElement<SteamWebApiAppDetailsResponse>(result.get(key = "$newAppId")!!)
    }

    private fun getUpdatedAppIdFromRedirectUrl(currentAppId: Int) : Int? {
        val regex = "https://store\\.steampowered\\.com/app/([0-9]*)"
        val url = getRedirectUrl("https://store.steampowered.com/app/$currentAppId")
        if (url.isNullOrBlank()) return null

        val pattern:Pattern  = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher:Matcher  = pattern.matcher(url)

        if (matcher.find()) {
            println("Full match: " + matcher.group(0))
            return matcher.group(1)?.toInt()
        }

        return null
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