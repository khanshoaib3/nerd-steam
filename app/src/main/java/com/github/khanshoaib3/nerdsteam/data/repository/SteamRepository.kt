package com.github.khanshoaib3.nerdsteam.data.repository

import android.content.res.Resources
import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import com.github.khanshoaib3.nerdsteam.data.model.api.AppDetailDataResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.AppDetailsResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.AppSearchResponse
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.Dlc
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.toDlc
import com.github.khanshoaib3.nerdsteam.data.remote.SteamCommunityApiService
import com.github.khanshoaib3.nerdsteam.data.remote.SteamInternalWebApiService
import com.github.khanshoaib3.nerdsteam.utils.runSafeSuspendCatching
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import retrofit2.http.Query
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.Locale
import javax.inject.Inject

private const val TAG = "SteamRepository"
private val json = Json { ignoreUnknownKeys = true }

interface SteamRepository {
    suspend fun runSearchQuery(query: String): Result<List<AppSearchResponse>>

    suspend fun fetchDataForAppId(appId: Int): Result<AppDetailDataResponse>

    suspend fun fetchDataForDlcs(dlcs: List<Int>): Result<List<Dlc>>
}

class OnlineSteamRepository @Inject constructor(
    private val steamCommunityApiService: SteamCommunityApiService,
    private val steamInternalWebApiService: SteamInternalWebApiService,
) : SteamRepository {
    override suspend fun runSearchQuery(query: String): Result<List<AppSearchResponse>> =
        runSafeSuspendCatching {
            Log.d(TAG, "Fetching search results with query: $query...")
            val result = steamCommunityApiService.searchApp(query)
            if (result.isEmpty()) throw Exception("Nothing found for the query")
            result
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetchDataForAppId(appId: Int): Result<AppDetailDataResponse> =
        fetchDataForAppId(appId = appId, isRetryAttempt = false)


    @Suppress("JoinDeclarationAndAssignment")
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchDataForAppId(
        appId: Int,
        isRetryAttempt: Boolean
    ): Result<AppDetailDataResponse> =
        runSafeSuspendCatching {
            var result: JsonObject
            lateinit var appDetail: AppDetailsResponse
            var missingField = false
            Log.d(TAG, "Fetching data from steam for $appId")

            result = if (isRetryAttempt) steamInternalWebApiService.getAppDetails(appId, "us")
            else steamInternalWebApiService.getAppDetails(appId)

            try {
                appDetail =
                    json.decodeFromJsonElement<AppDetailsResponse>(result.get(key = "$appId")!!)
            } catch (_: MissingFieldException) {
                missingField = true
            }

            if (!missingField && appDetail.success && appDetail.data != null) {
                Log.d(TAG, "Data fetched: ${appDetail.data.name}")
                return@runSafeSuspendCatching appDetail.data
            }

            Log.d(TAG, "Unable to serialize data for $appId because of missing fields.")
            Log.d(TAG, "Attempting to fetch updated App ID from redirect url...")

            val newAppId = getUpdatedAppIdFromRedirectUrl(appId)
            if (newAppId == null) throw Exception("Unable to fetch data for appid: $appId")
            Log.d(TAG, "New app id: $newAppId")

            result = if (isRetryAttempt) steamInternalWebApiService.getAppDetails(newAppId, "us")
            else steamInternalWebApiService.getAppDetails(newAppId)

            if (isRetryAttempt) {
                appDetail =
                    json.decodeFromJsonElement<AppDetailsResponse>(result.get(key = "$newAppId")!!)
            } else {
                missingField = false
                try {
                    appDetail =
                        json.decodeFromJsonElement<AppDetailsResponse>(result.get(key = "$newAppId")!!)
                } catch (_: MissingFieldException) {
                    missingField = true
                }

                if (missingField || !appDetail.success || appDetail.data == null) {
                    Log.d(TAG, "Unable to serialize data with new appid $newAppId.")
                    Log.d(TAG, "Retrying with us country code...")
                    return fetchDataForAppId(appId = appId, isRetryAttempt = true)
                }
            }
            if (!appDetail.success || appDetail.data == null) {
                throw Exception("Unable to fetch data for appid: $appId")
            }

            Log.d(TAG, "Data fetched: ${appDetail.data.name}")
            appDetail.data
        }

    override suspend fun fetchDataForDlcs(dlcs: List<Int>): Result<List<Dlc>> =
        runSafeSuspendCatching {
            buildList {
                dlcs.forEach { appId ->
                    fetchDataForAppId(appId)
                        .onSuccess { add(it.toDlc()) }
                        .onFailure { Log.e(TAG, "Unable to fetch dlc info with id: $appId") }
                }
            }
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