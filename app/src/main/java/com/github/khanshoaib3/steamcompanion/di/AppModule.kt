package com.github.khanshoaib3.steamcompanion.di
// Ref(dagger-hilt): https://www.youtube.com/watch?v=bbMsuI2p1DQ
// Ref: https://developer.android.com/training/dependency-injection/hilt-android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.khanshoaib3.steamcompanion.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.steamcompanion.data.local.MainDatabase
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopGameDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopRecordDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TrendingGameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideLocalDatastoreRepository(@ApplicationContext context: Context): LocalDataStoreRepository {
        return LocalDataStoreRepository(context.dataStore)
    }
}