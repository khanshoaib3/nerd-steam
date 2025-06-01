package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.local.detail.PriceTrackingDao
import com.github.khanshoaib3.steamcompanion.data.model.detail.PriceTracking
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
import javax.inject.Inject

private const val TAG = "GameDetailRepository"

interface GameDetailRepository {
    suspend fun fetchDataForAppId(appId: Int): SteamWebApiAppDetailsResponse?

    suspend fun trackPrice(priceTracking: PriceTracking)

    suspend fun getPriceTrackingInfo(appId: Int) : PriceTracking?

    suspend fun stopTracking(appId: Int)
}

class OnlineGameDetailRepository @Inject constructor(
    private val steamInternalWebApiService: SteamInternalWebApiService,
    private val priceTrackingDao: PriceTrackingDao
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

    override suspend fun trackPrice(priceTracking: PriceTracking) {
        priceTrackingDao.insert(priceTracking)
    }

    override suspend fun getPriceTrackingInfo(appId: Int) : PriceTracking? {
        if (!priceTrackingDao.doesExist(appId))
            return null

        return priceTrackingDao.getOne(appId)
    }

    override suspend fun stopTracking(appId: Int) {
        priceTrackingDao.deleteWithId(appId)
    }
}