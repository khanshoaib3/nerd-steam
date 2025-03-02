package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
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

private const val TAG = "FeedGamesRepository"

interface SteamChartsRepository {
    suspend fun fetchAndStoreData()

    suspend fun getAllTrendingGames(): Flow<List<TrendingGame>>

    suspend fun getAllTopGames(): Flow<List<TopGame>>

    suspend fun getAllTopRecords(): Flow<List<TopRecord>>
}

class ScraperSteamChartsRepository(
    private val trendingGameDao: TrendingGameDao,
    private val topGameDao: TopGameDao,
    private val topRecordDao: TopRecordDao
) : SteamChartsRepository {
    override suspend fun fetchAndStoreData() {
        if (trendingGameDao.getCount() != 0
            && topGameDao.getCount() != 0
            && topRecordDao.getCount() != 0) {
            Log.d(TAG, "Records already present")
            return
        }

        val rawData = SteamChartsScraper().scrape()
        trendingGameDao.insertMany(rawData.parseAndGetTrendingGamesList())
        topGameDao.insertMany(rawData.parseAndGetTopGamesList())
        topRecordDao.insertMany(rawData.parseAndGetTopRecordsList())
        Log.d(TAG, "Added records to the tables.")
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