package com.github.khanshoaib3.steamcompanion.di
// Ref(dagger-hilt): https://www.youtube.com/watch?v=bbMsuI2p1DQ
// Ref: https://developer.android.com/training/dependency-injection/hilt-android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.khanshoaib3.steamcompanion.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.steamcompanion.data.local.MainDatabase
import com.github.khanshoaib3.steamcompanion.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopGameDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopRecordDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TrendingGameDao
import com.github.khanshoaib3.steamcompanion.data.remote.SteamCommunityApiService
import com.github.khanshoaib3.steamcompanion.data.remote.SteamInternalWebApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

private const val DATASTORE_NAME = "steam_companion_data_store"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME
)

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideTrendingGameDao(@ApplicationContext context: Context): TrendingGameDao {
        return MainDatabase.getDatabase(context).trendingGameDao()
    }

    @Provides
    @Singleton
    fun provideTopGameDao(@ApplicationContext context: Context): TopGameDao {
        return MainDatabase.getDatabase(context).topGameDao()
    }

    @Provides
    @Singleton
    fun provideTopRecordDao(@ApplicationContext context: Context): TopRecordDao {
        return MainDatabase.getDatabase(context).topRecordDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(@ApplicationContext context: Context): BookmarkDao {
        return MainDatabase.getDatabase(context).bookmarkDao()
    }

    @Provides
    @Singleton
    fun provideLocalDatastoreRepository(@ApplicationContext context: Context): LocalDataStoreRepository {
        return LocalDataStoreRepository(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideSteamCommunityService(): SteamCommunityApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://steamcommunity.com")
            .build()
            .create(SteamCommunityApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSteamInternalWebApiService(): SteamInternalWebApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://store.steampowered.com")
            .build()
            .create(SteamInternalWebApiService::class.java)
    }
}