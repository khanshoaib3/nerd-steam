package com.github.khanshoaib3.steamcompanion.data.scraper

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tbody
import it.skrape.selects.html5.td
import it.skrape.selects.html5.tr
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

private const val TAG = "SteamChartsScraper"

data class SteamChartsScrapedData(
    var httpStatusCode: Int = 0,
    var httpStatusMessage: String = "",
    var trendingGames: List<List<String>> = emptyList(),
    var topGames: List<List<String>> = emptyList(),
    var topRecords: List<List<String>> = emptyList(),
)

class SteamChartsScraper(private val URL: String = "https://steamcharts.com/") {
    fun scrape(): SteamChartsScrapedData {
        val extracted = skrape(HttpFetcher) {
            request {
                url = URL
            }
            extractIt<SteamChartsScrapedData> {
                it.httpStatusCode = status { code }
                it.httpStatusMessage = status { message }
                htmlDocument {
                    var trendingGames = mutableListOf<List<String>>()
                    var topGames = mutableListOf<List<String>>()
                    var topRecords = mutableListOf<List<String>>()

                    table {
                        withId = "trending-recent"
                        findFirst {
                            tbody {
                                tr {
                                    findAll {
                                        forEach { row ->
                                            trendingGames.add(
                                                listOf(
                                                    row.td { findByIndex(0) { a { findFirst { attribute("href") } } } }, // AppID
                                                    row.td { findByIndex(0) { text } }, // Name
                                                    row.td { findByIndex(1) { text } }, // Gain/24 hours Change
                                                    row.td { findByIndex(3) { text } }, // Current Players
                                                    // TODO Try to add the graph
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    table {
                        withId = "top-games"
                        findFirst {
                            tbody {
                                tr {
                                    findAll {
                                        forEach { row ->
                                            topGames.add(
                                                listOf(
                                                    row.td { findByIndex(1) { a { findFirst { attribute("href") } } } }, // AppID
                                                    row.td { findByIndex(1) { text } }, // Name
                                                    row.td { findByIndex(2) { text } }, // Current Players
                                                    row.td { findByIndex(4) { text } }, // Peak Players
                                                    row.td { findByIndex(5) { text } }, // Hours Played
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    table {
                        withId = "toppeaks"
                        findFirst {
                            tbody {
                                tr {
                                    findAll {
                                        forEach { row ->
                                            topRecords.add(
                                                listOf(
                                                    row.td { findByIndex(0) { a { findFirst { attribute("href") } } } }, // AppID
                                                    row.td { findByIndex(0) { text } }, // Name
                                                    row.td { findByIndex(1) { text } }, // Peak Players
                                                    row.td { findByIndex(2) { text } }, // Time
                                                    // TODO Try to add the graph
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    it.trendingGames = trendingGames
                    it.topGames = topGames
                    it.topRecords = topRecords
                }
            }
        }

        Log.d(TAG, extracted.trendingGames.toString())
        Log.d(TAG, extracted.topGames.toString())
        Log.d(TAG, extracted.topRecords.toString())

        return extracted
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
            hours = it[4]
        )
    }



fun SteamChartsScrapedData.parseAndGetTopRecords(): List<TopRecord> {
    val formatter = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); year()
    }

    return topRecords.map {
        TopRecord(
            appId = it[0].substring(it[0].lastIndexOf("/") + 1).toInt(),
            name = it[1],
            peakPlayers = it[2].toInt(),
            time = formatter.format(LocalDate.parse(it[3].substring(0, it[3].indexOf("T"))))
        )
    }
}