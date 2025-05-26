package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "SearchRepository"

interface SearchRepository {
    fun runSearchQuery(): Flow<List<Bookmark>>
}

class RemoteSearchRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : SearchRepository {
    override fun runSearchQuery(): Flow<List<Bookmark>> {
        Log.d(TAG, "Fetching search results...")
        return bookmarkDao.getAll()
    }
}
