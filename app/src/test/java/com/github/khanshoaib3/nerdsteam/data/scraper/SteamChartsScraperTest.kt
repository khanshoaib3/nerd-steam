package com.github.khanshoaib3.nerdsteam.data.scraper

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class SteamChartsScraperTest {
    @Test
    fun `Test per app player stats`() {
        runBlocking {
            val response = SteamChartsPerAppScraper(570).scrape()
            Assert.assertNotEquals(response.monthlyPlayerStats.size, 0)
            println(response)
        }
    }

    @Test
    fun `Test home page scraper`() {
        runBlocking {
            val response = SteamChartsScraper().scrape()
            Assert.assertEquals(response.trendingGames.size, 5)
            Assert.assertEquals(response.topGames.size, 10)
            Assert.assertEquals(response.topRecords.size, 10)
            println(response)
        }
    }
}
