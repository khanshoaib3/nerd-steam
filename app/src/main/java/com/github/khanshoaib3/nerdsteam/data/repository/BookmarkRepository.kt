package com.github.khanshoaib3.nerdsteam.data.repository

import com.github.khanshoaib3.nerdsteam.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.nerdsteam.data.model.bookmark.Bookmark
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

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
        Timber.d("Fetching bookmarks...")
        return bookmarkDao.getAll()
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        if (isBookmarked(bookmark.appId)) return

        Timber.d("Inserting a bookmark with appid ${bookmark.appId}...")
        bookmarkDao.insert(bookmark)
    }

    override suspend fun removeBookmark(appId: Int?) {
        if (appId == null) return
        if (!isBookmarked(appId)) return

        Timber.d("Deleting a bookmark with appid ${appId}...")
        bookmarkDao.deleteByAppId(appId)
    }

    override suspend fun isBookmarked(appId: Int?): Boolean {
        if (appId == null) return false
        return bookmarkDao.doesExist(appId)
    }
}