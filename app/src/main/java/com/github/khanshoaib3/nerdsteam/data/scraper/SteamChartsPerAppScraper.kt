package com.github.khanshoaib3.nerdsteam.data.scraper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import timber.log.Timber
import kotlin.collections.listOf

data class MonthlyPlayerStatistic(
    val month: String,
    val avgPlayers: String,
    val gain: String,
    val percGain: String,
    val peakPlayers: String,
)

data class SteamChartsPerAppScrapedData(
    var monthlyPlayerStats: List<MonthlyPlayerStatistic> = listOf(),
    var lastHourCount: String = "",
    var lastHourTime: String = "",
    var twentyFourHourPeak: String = "",
    var allTimePeak: String = "",
)

class SteamChartsPerAppScraper(private val appId: Int) {
    suspend fun scrape(): SteamChartsPerAppScrapedData = withContext(Dispatchers.IO) {
        Timber.d("Started scraping (https://steamcharts.com/app/$appId)...")
        val document = Jsoup.connect("https://steamcharts.com/app/$appId").get()
        val monthlyPlayerStats = buildList {
            document.select(".common-table tbody tr").forEach { tr ->
                tr.select("td").let { tds ->
                    add(
                        MonthlyPlayerStatistic(
                            month = tds[0].text(),
                            avgPlayers = tds[1].text(),
                            gain = tds[2].text(),
                            percGain = tds[3].text(),
                            peakPlayers = tds[4].text(),
                        )
                    )
                }
            }
        }

        val lastHourCount: String
        val lastHourTime: String
        val twentyFourHourPeak: String
        val allTimePeak: String
        document.select("#app-heading .app-stat").let { classes ->
            lastHourCount = classes[0].select("span").text()
            lastHourTime = classes[0].select("abbr").attr("title")
            twentyFourHourPeak = classes[1].select("span").text()
            allTimePeak = classes[2].select("span").text()
        }

        Timber.d("Data fetched successfully!")
        return@withContext SteamChartsPerAppScrapedData(
            monthlyPlayerStats = monthlyPlayerStats,
            lastHourCount = lastHourCount,
            lastHourTime = lastHourTime,
            twentyFourHourPeak = twentyFourHourPeak,
            allTimePeak = allTimePeak,
        )
    }
}
