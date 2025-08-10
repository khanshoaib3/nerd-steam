package com.github.khanshoaib3.nerdsteam.data.remote

import kotlinx.serialization.json.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

// Ref: https://github.com/Revadike/InternalSteamWebAPI/
interface SteamInternalWebApiService {
    // TODO Add country/region selection in settings
    @GET("/api/appdetails")
    suspend fun getAppDetails(@Query("appids") appId: Int, @Query("cc") countryCode: String = "in"): JsonObject
}