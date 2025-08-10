package com.github.khanshoaib3.nerdsteam.data.repository

import android.util.Log
import com.github.khanshoaib3.nerdsteam.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.nerdsteam.data.model.bookmark.Bookmark
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "BookmarkRepository"

interface BookmarkRepository {
    fun getAllBookmarks(): Flow<List<Bookmark>>

    suspend fun addBookmark(bookmark: Bookmark)

    suspend fun removeBookmark(appId: Int?)

    suspend fun isBookmarked(appId: Int?): Boolean
}

class LocalBookmarkRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {
    override fun getAllBookmarks(): Flow<List<Bookmark>> {
        Log.d(TAG, "Fetching bookmarks...")
        return bookmarkDao.getAll()
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        if (isBookmarked(bookmark.appId)) return

        Log.d(TAG, "Inserting a bookmark with appid ${bookmark.appId}...")
        bookmarkDao.insert(bookmark)
    }

    override suspend fun removeBookmark(appId: Int?) {
        if (appId == null) return
        if (!isBookmarked(appId)) return

        Log.d(TAG, "Deleting a bookmark with appid ${appId}...")
        bookmarkDao.deleteByAppId(appId)
    }

    override suspend fun isBookmarked(appId: Int?): Boolean {
        if (appId == null) return false
        return bookmarkDao.doesExist(appId)
    }
}