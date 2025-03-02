package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.model.TrendingGame
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsScraper
import com.github.khanshoaib3.steamcompanion.data.scraper.parseAndGetTopGamesList
import com.github.khanshoaib3.steamcompanion.data.scraper.parseAndGetTopRecords
import com.github.khanshoaib3.steamcompanion.data.scraper.parseAndGetTrendingGamesList

private const val TAG = "FeedGamesRepository"

interface FeedGamesRepository {
    suspend fun fetchAndStoreData()

    suspend fun getAllTrendingGames(): List<TrendingGame>

    suspend fun getAllTopGames(): List<String>

    suspend fun getAllTopRecords(): List<String>
}

class ScraperFeedGamesRepository : FeedGamesRepository {
    override suspend fun fetchAndStoreData() {
        val rawData = SteamChartsScraper().scrape()
        Log.d(TAG, rawData.parseAndGetTrendingGamesList().toString())
        Log.d(TAG, rawData.parseAndGetTopGamesList().toString())
        Log.d(TAG, rawData.parseAndGetTopRecords().toString())
    }

    override suspend fun getAllTrendingGames(): List<TrendingGame> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTopGames(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTopRecords(): List<String> {
        TODO("Not yet implemented")
    }
}