package com.github.khanshoaib3.nerdsteam.data.model.appdetail

import com.github.khanshoaib3.nerdsteam.data.scraper.SteamChartsPerAppScrapedData
import java.time.Instant

data class PlayerStatistics(
    val lastHourCount: Int,
    val lastHourTime: Instant,
    val twentyFourHourPeak: Int,
    val allTimePeak: Int,
    val perMonthPlayerStats: List<MonthlyPlayerStatistic>,
)

data class MonthlyPlayerStatistic(
    val month: String,
    val avgPlayers: String,
    val gain: String,
    val percGain: String,
    val peakPlayers: String,
)

fun SteamChartsPerAppScrapedData.toPlayerStatistics() =
    PlayerStatistics(
        lastHourCount = this.lastHourCount.toInt(),
        lastHourTime = Instant.parse(this.lastHourTime),
        twentyFourHourPeak = this.twentyFourHourPeak.toInt(),
        allTimePeak = this.allTimePeak.toInt(),
        perMonthPlayerStats = this.monthlyPlayerStats
    )
