package com.github.khanshoaib3.steamcompanion.data.scraper

import kotlinx.coroutines.runBlocking
import org.junit.Test

class SteamChartsScraperTest {
    @Test
    fun `Test per app player stats`() {
        runBlocking {
            val response = SteamChartsPerAppScraper(730).scrape()
            println(response)
        }
    }
}
