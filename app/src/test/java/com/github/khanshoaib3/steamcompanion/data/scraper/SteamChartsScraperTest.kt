package com.github.khanshoaib3.steamcompanion.data.scraper

import com.github.marlonlom.utilities.timeago.TimeAgo
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Instant

class SteamChartsScraperTest {
    @Test
    fun `Test per app player stats`() {
        runBlocking {
            val response = SteamChartsPerAppScraper(730).scrape()
            println(response)
        }
    }

    @Test
    fun `Test time conversion`() {
        val rawTime = "2025-08-01T19:01:34Z"
        val ee = Instant.parse(rawTime).toEpochMilli()
        println(TimeAgo.using(ee))
    }
}