package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.steamcompanion.data.model.api.AppSearchResponse
import com.github.khanshoaib3.steamcompanion.data.remote.SteamCommunityApiService
import javax.inject.Inject

private const val TAG = "SearchRepository"

interface SearchRepository {
    suspend fun runSearchQuery(query: String): List<AppSearchResponse>
}

class RemoteSearchRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val steamCommunityApiService: SteamCommunityApiService,
) : SearchRepository {
    override suspend fun runSearchQuery(query: String): List<AppSearchResponse> {
        Log.d(TAG, "Fetching search results...")
        val result = steamCommunityApiService.searchApp(query)
        // TODO Add exception handling
        return result
    }
}
