package com.github.khanshoaib3.steamcompanion.data.scraper

import android.util.Log
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tbody
import it.skrape.selects.html5.td
import it.skrape.selects.html5.tr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

private const val TAG = "SteamChartsPerAppScraper"

data class PlayerStatsRowData(
    val month: String,
    val avgPlayers: String,
    val gain: String,
    val percGain: String,
    val peakPlayers: String,
)

data class SteamChartsPerAppScrapedData(
    var httpStatusCode: Int = 0,
    var httpStatusMessage: String = "",
    var playerStatsRowData: List<PlayerStatsRowData> = listOf(),
)

class SteamChartsPerAppScraper(private val appId: Int) {
    @Throws
    suspend fun scrape(): SteamChartsPerAppScrapedData = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Started scraping...")
            val extracted = skrape(HttpFetcher) {
                request {
                    url = "https://steamcharts.com/app/$appId"
                }
                extractIt<SteamChartsPerAppScrapedData> {
                    it.httpStatusCode = status { code }
                    it.httpStatusMessage = status { message }
                    htmlDocument {
                        var rows = mutableListOf<PlayerStatsRowData>()
                        table {
                            withClass = "common-table"
                            findFirst {
                                tbody {
                                    tr {
                                        findAll {
                                            forEach { row ->
                                                rows.add(
                                                    PlayerStatsRowData(
                                                        month = row.td { findByIndex(0) { text } },
                                                        avgPlayers = row.td { findByIndex(1) { text } },
                                                        gain = row.td { findByIndex(2) { text } },
                                                        percGain = row.td { findByIndex(3) { text } },
                                                        peakPlayers = row.td { findByIndex(4) { text } }
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        it.playerStatsRowData = rows
                    }
                }
            }

            Log.d(TAG, "Data fetched successfully!")
            return@withContext extracted
        } catch (e: Exception) {
            Log.e(TAG, "Unable to fetch data for app id - $appId")
            e.printStackTrace()
            throw e
        }
    }
}
