package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.model.search.AppSearchResult
import com.github.khanshoaib3.steamcompanion.data.remote.SteamCommunityApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "SearchRepository"

interface SearchRepository {
    suspend fun runSearchQuery(query: String): List<AppSearchResult>
}

class RemoteSearchRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val steamCommunityApiService: SteamCommunityApiService,
) : SearchRepository {
    override suspend fun runSearchQuery(query: String): List<AppSearchResult> {
        Log.d(TAG, "Fetching search results...")
        val result = steamCommunityApiService.searchApp(query)
        // TODO Add exception handling
        return result
    }
}
