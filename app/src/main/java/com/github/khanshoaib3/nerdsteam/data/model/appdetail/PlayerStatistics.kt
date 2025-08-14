package com.github.khanshoaib3.nerdsteam.data.model.appdetail

import com.github.khanshoaib3.nerdsteam.data.scraper.MonthlyPlayerStatistic
import com.github.khanshoaib3.nerdsteam.data.scraper.SteamChartsPerAppScrapedData
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class PlayerStatistics @OptIn(ExperimentalTime::class) constructor(
    val lastHourCount: Int,
    val lastHourTime: Long,
    val twentyFourHourPeak: Int,
    val allTimePeak: Int,
    val perMonthPlayerStats: List<MonthlyPlayerStatisticDisplay>,
)

data class MonthlyPlayerStatisticDisplay(
    val month: String,
    val avgPlayers: Double,
    val gain: String,
    val percGain: String,
    val peakPlayers: Double,
)

fun String.toShortMonth(): String {
    val splits = this.split(" ")
    if (splits.size != 2) return this
    val monthName = when (splits[0].lowercase()) {
        "january" -> "Jan"
        "february" -> "Feb"
        "march" -> "Mar"
        "april" -> "Apr"
        "may" -> "May"
        "june" -> "Jun"
        "july" -> "Jul"
        "august" -> "Aug"
        "september" -> "Sep"
        "october" -> "Oct"
        "november" -> "Nov"
        "december" -> "Dec"
        else -> this
    }
    return "$monthName ${splits[1]}"
}

fun MonthlyPlayerStatistic.toDisplayModel() = MonthlyPlayerStatisticDisplay(
    month = this.month.toShortMonth(),
    avgPlayers = this.avgPlayers.toDouble(),
    gain = this.gain,
    percGain = this.percGain,
    peakPlayers = this.peakPlayers.toDouble(),
)

@OptIn(ExperimentalTime::class)
fun SteamChartsPerAppScrapedData.toPlayerStatistics() =
    PlayerStatistics(
        lastHourCount = this.lastHourCount.toInt(),
        lastHourTime = Instant.parse(this.lastHourTime).toEpochMilliseconds(),
        twentyFourHourPeak = this.twentyFourHourPeak.toInt(),
        allTimePeak = this.allTimePeak.toInt(),
        perMonthPlayerStats = this.monthlyPlayerStats.map { it.toDisplayModel() }
    )
