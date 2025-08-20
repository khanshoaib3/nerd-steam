package com.github.khanshoaib3.nerdsteam.data.scraper

import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TopGame
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.nerdsteam.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import timber.log.Timber

data class SteamChartsScrapedData(
    var trendingGames: List<List<String>> = emptyList(),
    var topGames: List<List<String>> = emptyList(),
    var topRecords: List<List<String>> = emptyList(),
)

class SteamChartsScraper(private val _url: String = "https://steamcharts.com/") {
    suspend fun scrape(): SteamChartsScrapedData = withContext(Dispatchers.IO) {
        Timber.d("Started scraping ($_url)...")
        val document = Jsoup.connect(_url).get()

        val trendingGames = buildList {
            document.select("#trending-recent tbody tr").forEach { tr ->
                tr.select("td").let { tds ->
                    add(
                        listOf(
                            tds[0].select("a").attr("href"), // AppId
                            tds[0].select("a").text(), // Name
                            tds[1].text(), // 24-Hour Change
                            tds[3].text(), // Current Players
                        )
                    )
                }
            }
        }

        val topGames = buildList {
            document.select("#top-games tbody tr").forEach { tr ->
                tr.select("td").let { tds ->
                    add(
                        listOf(
                            tds[1].select("a").attr("href"), // AppId
                            tds[1].select("a").text(), // Name
                            tds[2].text(), // Current Players
                            tds[4].text(), // Peak Players
                            tds[5].text(), // Hours Played
                        )
                    )
                }
            }
        }

        val topRecords = buildList {
            document.select("#toppeaks tbody tr").forEach { tr ->
                tr.select("td").let { tds ->
                    add(
                        listOf(
                            tds[0].select("a").attr("href"), // AppId
                            tds[0].select("a").text(), // Name
                            tds[1].text(), // Peak Players
                            tds[2].text(), // Time
                        )
                    )
                }
            }
        }

        Timber.d("Data found!")
        return@withContext SteamChartsScrapedData(
            trendingGames = trendingGames,
            topGames = topGames,
            topRecords = topRecords,
        )
    }
}

fun SteamChartsScrapedData.parseAndGetTrendingGamesList(): List<TrendingGame> =
    trendingGames.map {
        TrendingGame(
            appId = it[0].substring(it[0].lastIndexOf("/") + 1).toInt(),
            name = it[1],
            gain = it[2],
            currentPlayers = it[3].toInt()
        )
    }

fun SteamChartsScrapedData.parseAndGetTopGamesList(): List<TopGame> =
    topGames.map {
        TopGame(
            appId = it[0].substring(it[0].lastIndexOf("/") + 1).toInt(),
            name = it[1],
            currentPlayers = it[2].toInt(),
            peakPlayers = it[3].toInt(),
            hours = it[4].toLong()
        )
    }


fun SteamChartsScrapedData.parseAndGetTopRecordsList(): List<TopRecord> {
    return topRecords.map {
        TopRecord(
            appId = it[0].substring(it[0].lastIndexOf("/") + 1).toInt(),
            name = it[1],
            peakPlayers = it[2].toInt(),
            month = DateTimeUtils.getConciseDate(it[3], includeDay = false) ?: "Jan 1999",
        )
    }
}
