package com.github.khanshoaib3.nerdsteam.data.scraper

import android.util.Log
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.MonthlyPlayerStatistic
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.abbr
import it.skrape.selects.html5.div
import it.skrape.selects.html5.span
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tbody
import it.skrape.selects.html5.td
import it.skrape.selects.html5.tr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "SteamChartsPerAppScraper"

data class SteamChartsPerAppScrapedData(
    var httpStatusCode: Int = 0,
    var httpStatusMessage: String = "",
    var monthlyPlayerStats: List<MonthlyPlayerStatistic> = listOf(),
    var lastHourCount: String = "",
    var lastHourTime: String = "",
    var twentyFourHourPeak: String = "",
    var allTimePeak: String = "",
)

class SteamChartsPerAppScraper(private val appId: Int) {
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
                        val rows = mutableListOf<MonthlyPlayerStatistic>()
                        table {
                            withClass = "common-table"
                            findFirst {
                                tbody {
                                    tr {
                                        findAll {
                                            forEach { row ->
                                                rows.add(
                                                    MonthlyPlayerStatistic(
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

                        it.monthlyPlayerStats = rows

                        div {
                            withId = "app-heading"
                            div {
                                withClass = "app-stat"
                                findAll {
                                    it.lastHourCount = get(0).span { findFirst { text } }
                                    it.lastHourTime = get(0).abbr {
                                        findFirst { attribute("title") }
                                    }
                                    it.twentyFourHourPeak = get(1).span { findFirst { text } }
                                    it.allTimePeak = get(2).span { findFirst { text } }
                                }
                            }
                        }
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
