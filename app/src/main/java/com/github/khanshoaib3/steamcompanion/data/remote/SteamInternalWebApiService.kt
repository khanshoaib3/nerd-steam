package com.github.khanshoaib3.steamcompanion.data.remote

import kotlinx.serialization.json.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

// Ref: https://github.com/Revadike/InternalSteamWebAPI/
interface SteamInternalWebApiService {
    @GET("/api/appdetails")
    suspend fun getAppDetails(@Query("appids") appId: Int): JsonObject
}