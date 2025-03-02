package com.github.khanshoaib3.steamcompanion.di

import android.content.Context
import com.github.khanshoaib3.steamcompanion.data.repository.SteamChartsRepository
import com.github.khanshoaib3.steamcompanion.data.repository.ScraperSteamChartsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val steamChartsRepository: SteamChartsRepository
}

/**
 * [AppContainer] implementation that provides instances of repositories
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val steamChartsRepository: SteamChartsRepository by lazy {
        ScraperSteamChartsRepository()
    }
}
