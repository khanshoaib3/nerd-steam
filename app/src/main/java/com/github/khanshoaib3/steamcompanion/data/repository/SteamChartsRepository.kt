package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopGameDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopRecordDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TrendingGameDao
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsScraper
import com.github.khanshoaib3.steamcompanion.data.scraper.parseAndGetTopGamesList
import com.github.khanshoaib3.steamcompanion.data.scraper.parseAndGetTopRecordsList
import com.github.khanshoaib3.steamcompanion.data.scraper.parseAndGetTrendingGamesList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "SteamChartsRepository"

interface SteamChartsRepository {
    suspend fun fetchAndStoreData()

    suspend fun getAllTrendingGames(): Flow<List<TrendingGame>>

    suspend fun getAllTopGames(): Flow<List<TopGame>>

    suspend fun getAllTopRecords(): Flow<List<TopRecord>>
}


private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH")

class ScraperSteamChartsRepository(
    private val trendingGameDao: TrendingGameDao,
    private val topGameDao: TopGameDao,
    private val topRecordDao: TopRecordDao,
    private val localDataStoreRepository: LocalDataStoreRepository
) : SteamChartsRepository {
    override suspend fun fetchAndStoreData() {
        val steamChartsFetchTime = localDataStoreRepository.steamChartsFetchTimeSnapshot()
        if (steamChartsFetchTime.isNotEmpty()) {
            val oldDateTime = LocalDateTime.parse(steamChartsFetchTime, formatter)
            val currentDateTime = LocalDateTime.now()

            if (currentDateTime.hour <= oldDateTime.hour
                && currentDateTime.dayOfYear <= oldDateTime.dayOfYear
                && currentDateTime.year <= oldDateTime.year
            ) {
                Log.d(TAG, "Records already present")
                return
            }

            trendingGameDao.deleteAll()
            topGameDao.deleteAll()
            topRecordDao.deleteAll()
            Log.d(TAG, "Deleted old records, adding new ones...")
        }

        val rawData = SteamChartsScraper().scrape()
        trendingGameDao.insertMany(rawData.parseAndGetTrendingGamesList())
        topGameDao.insertMany(rawData.parseAndGetTopGamesList())
        topRecordDao.insertMany(rawData.parseAndGetTopRecordsList())

        val timeStamp = LocalDateTime.now().format(formatter)
        localDataStoreRepository.saveData(timeStamp)
        Log.d(TAG, "Added records to the tables. with timestamp $timeStamp")
    }

    override suspend fun getAllTrendingGames(): Flow<List<TrendingGame>> {
        return trendingGameDao.getAll()
    }

    override suspend fun getAllTopGames(): Flow<List<TopGame>> {
        return topGameDao.getAll()
    }

    override suspend fun getAllTopRecords(): Flow<List<TopRecord>> {
        return topRecordDao.getAll()
    }
}