package com.github.khanshoaib3.steamcompanion.data.repository

import com.github.khanshoaib3.steamcompanion.data.model.TrendingGame
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsScraper

interface FeedGamesRepository {
    suspend fun fetchAndStoreData()

    suspend fun getAllTrendingGames(): List<TrendingGame>

    suspend fun getAllTopGames(): List<String>

    suspend fun getAllTopRecords(): List<String>
}

class ScraperTrendingGamesRepository : FeedGamesRepository {
    override suspend fun fetchAndStoreData() {
        val scraper = SteamChartsScraper()
        scraper.scrape()
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