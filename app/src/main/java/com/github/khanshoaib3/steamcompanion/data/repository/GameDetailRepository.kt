package com.github.khanshoaib3.steamcompanion.data.repository

import com.github.khanshoaib3.steamcompanion.data.model.detail.SteamWebApiAppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.remote.SteamInternalWebApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject

interface GameDetailRepository {
    suspend fun fetchDataForAppId(appId: Int) : SteamWebApiAppDetailsResponse
}

class OnlineGameDetailRepository @Inject constructor(
    private val steamInternalWebApiService: SteamInternalWebApiService,
) : GameDetailRepository {
    override suspend fun fetchDataForAppId(appId: Int) : SteamWebApiAppDetailsResponse {
        val result = steamInternalWebApiService.getAppDetails(appId)
        val json = Json { ignoreUnknownKeys = true }

        return json.decodeFromJsonElement<SteamWebApiAppDetailsResponse>(result.get(key = "$appId")!!)
    }
}