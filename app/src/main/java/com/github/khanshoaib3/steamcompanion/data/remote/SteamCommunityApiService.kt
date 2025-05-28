package com.github.khanshoaib3.steamcompanion.data.remote

import com.github.khanshoaib3.steamcompanion.data.model.search.AppSearchResult
import kotlinx.serialization.json.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Ref: https://github.com/Revadike/InternalSteamWebAPI/
interface SteamCommunityApiService {
    @GET("/actions/SearchApps/{query}")
    suspend fun searchApp(@Path("query") query: String) : List<AppSearchResult>
}