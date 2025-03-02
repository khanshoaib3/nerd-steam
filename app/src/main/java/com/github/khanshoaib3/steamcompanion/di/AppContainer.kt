package com.github.khanshoaib3.steamcompanion.di

import android.content.Context
import com.github.khanshoaib3.steamcompanion.data.repository.FeedGamesRepository
import com.github.khanshoaib3.steamcompanion.data.repository.ScraperFeedGamesRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val feedGamesRepository: FeedGamesRepository
}

/**
 * [AppContainer] implementation that provides instances of repositories
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val feedGamesRepository: FeedGamesRepository by lazy {
        ScraperFeedGamesRepository()
    }
}
