package com.github.khanshoaib3.steamcompanion.data.remote

import com.github.khanshoaib3.steamcompanion.data.model.api.AppSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path

// Ref: https://github.com/Revadike/InternalSteamWebAPI/
interface SteamCommunityApiService {
    @GET("/actions/SearchApps/{query}")
    suspend fun searchApp(@Path("query") query: String) : List<AppSearchResponse>
}