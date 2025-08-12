package com.github.khanshoaib3.nerdsteam.data.repository

import android.util.Log
import com.github.khanshoaib3.nerdsteam.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.nerdsteam.data.local.steamcharts.TopGameDao
import com.github.khanshoaib3.nerdsteam.data.local.steamcharts.TopRecordDao
import com.github.khanshoaib3.nerdsteam.data.local.steamcharts.TrendingGameDao
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TopGame
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.nerdsteam.data.scraper.SteamChartsPerAppScrapedData
import com.github.khanshoaib3.nerdsteam.data.scraper.SteamChartsPerAppScraper
import com.github.khanshoaib3.nerdsteam.data.scraper.SteamChartsScraper
import com.github.khanshoaib3.nerdsteam.data.scraper.parseAndGetTopGamesList
import com.github.khanshoaib3.nerdsteam.data.scraper.parseAndGetTopRecordsList
import com.github.khanshoaib3.nerdsteam.data.scraper.parseAndGetTrendingGamesList
import com.github.khanshoaib3.nerdsteam.utils.runSafeSuspendCatching
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val TAG = "SteamChartsRepository"

interface SteamChartsRepository {
    suspend fun fetchAndStoreData(): Result<Unit>

    fun getAllTrendingGames(): Flow<List<TrendingGame>>

    fun getAllTopGames(): Flow<List<TopGame>>

    fun getAllTopRecords(): Flow<List<TopRecord>>

    suspend fun fetchDataForApp(appId: Int): Result<SteamChartsPerAppScrapedData>
}


private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH")

class ScraperSteamChartsRepository @Inject constructor(
    private val trendingGameDao: TrendingGameDao,
    private val topGameDao: TopGameDao,
    private val topRecordDao: TopRecordDao,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : SteamChartsRepository {
    override suspend fun fetchAndStoreData(): Result<Unit> = runSafeSuspendCatching {
        val steamChartsFetchTime = localDataStoreRepository.steamChartsFetchTimeSnapshot()
        if (steamChartsFetchTime.isNotEmpty()) {
            val oldDateTime = LocalDateTime.parse(steamChartsFetchTime, formatter)
            val currentDateTime = LocalDateTime.now()

            if (oldDateTime.plusHours(1).isAfter(currentDateTime)) {
                Log.d(TAG, "Records already present, with timestamp $steamChartsFetchTime")
                return@runSafeSuspendCatching
            }

            // Deleting the contents does not reset the primary key index,
            // and since we can't use DROP, we have to manually delete the primary index as well.
            // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
            trendingGameDao.deleteAll()
            trendingGameDao.deletePrimaryKeyIndex()
            topGameDao.deleteAll()
            topGameDao.deletePrimaryKeyIndex()
            topRecordDao.deleteAll()
            topRecordDao.deletePrimaryKeyIndex()
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

    override fun getAllTrendingGames(): Flow<List<TrendingGame>> {
        return trendingGameDao.getAll()
    }

    override fun getAllTopGames(): Flow<List<TopGame>> {
        return topGameDao.getAll()
    }

    override fun getAllTopRecords(): Flow<List<TopRecord>> {
        return topRecordDao.getAll()
    }

    override suspend fun fetchDataForApp(appId: Int) = runSafeSuspendCatching {
        SteamChartsPerAppScraper(appId = appId).scrape()
    }
}