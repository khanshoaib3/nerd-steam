package com.github.khanshoaib3.steamcompanion.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.khanshoaib3.steamcompanion.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.steamcompanion.data.local.MainDatabase
import com.github.khanshoaib3.steamcompanion.data.repository.SteamChartsRepository
import com.github.khanshoaib3.steamcompanion.data.repository.ScraperSteamChartsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val steamChartsRepository: SteamChartsRepository
    val localDataStoreRepository: LocalDataStoreRepository
}

private const val DATASTORE_NAME = "steam_companion_data_store"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME
)


/**
 * [AppContainer] implementation that provides instances of repositories
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val steamChartsRepository: SteamChartsRepository by lazy {
        ScraperSteamChartsRepository(
            trendingGameDao = MainDatabase.getDatabase(context).trendingGameDao(),
            topGameDao = MainDatabase.getDatabase(context).topGameDao(),
            topRecordDao = MainDatabase.getDatabase(context).topRecordDao(),
            localDataStoreRepository = localDataStoreRepository
        )
    }

    override val localDataStoreRepository: LocalDataStoreRepository by lazy {
        LocalDataStoreRepository(context.dataStore)
    }
}
