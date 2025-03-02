package com.github.khanshoaib3.steamcompanion.data.scraper

import android.util.Log
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tbody
import it.skrape.selects.html5.td
import it.skrape.selects.html5.tr

data class SteamChartsScrapedData(
    var httpStatusCode: Int = 0,
    var httpStatusMessage: String = "",
    var trendingGames: List<List<String>> = emptyList(),
    var topGames: List<List<String>> = emptyList(),
    var topRecords: List<List<String>> = emptyList(),
)

class SteamChartsScraper {
    fun scrape() : SteamChartsScrapedData {
        val extracted = skrape(HttpFetcher) {
            request {
                url = "https://steamcharts.com/"
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
                                                    row.td { findByIndex(0) { a { findFirst { attribute("href") } } } },
                                                    row.td { findByIndex(0) { text } },
                                                    row.td { findByIndex(1) { text } },
                                                    row.td { findByIndex(3) { text } },
                                                )
                                            )

//                                            Log.d("SCRAPER", row.td { findByIndex(0) { a { findFirst { attribute("href") } } } })
//                                            Log.d("SCRAPER", row.td { findByIndex(0) { text } })
//                                            Log.d("SCRAPER", row.td { findByIndex(1) { text } })
//                                            Log.d("SCRAPER", row.td { findByIndex(3) { text } })
//                                            Log.d( "SCRAPER", row.td { findByIndex(2) { "svg" { findFirst { text } } } })
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
                                                    row.td { findByIndex(1) { a { findFirst { attribute("href") } } } },
                                                    row.td { findByIndex(1) { text } },
                                                    row.td { findByIndex(2) { text } },
                                                    row.td { findByIndex(4) { text } },
                                                    row.td { findByIndex(5) { text } },
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
                                                    row.td { findByIndex(0) { a { findFirst { attribute("href") } } } },
                                                    row.td { findByIndex(0) { text } },
                                                    row.td { findByIndex(1) { text } },
                                                    row.td { findByIndex(2) { text } },
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
//        td {
//            findAll {
//                Log.d("SCRAPER", findByIndex(0) { text })
//                Log.d("SCRAPER", findByIndex(1) { text })
//                Log.d("SCRAPER", findByIndex(3) { text })
//                Log.d( "SCRAPER", findByIndex(2) { "path" { findFirst { attribute("d") } } })
//            }
//        }

        Log.d("SCRAPER", extracted.trendingGames.toString())
        Log.d("SCRAPER", extracted.topGames.toString())
        Log.d("SCRAPER", extracted.topRecords.toString())

        return extracted
    }
}