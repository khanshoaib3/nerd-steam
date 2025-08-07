package com.github.khanshoaib3.steamcompanion.data.model.appdetail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "price_alerts")
data class PriceAlert @OptIn(ExperimentalTime::class) constructor(
    @ColumnInfo(name = "appid")
    @PrimaryKey val appId: Int,

    @ColumnInfo(name = "game_name")
    val name: String,

    @ColumnInfo(name = "target_price")
    val targetPrice: Float,

    @ColumnInfo(name = "notify_every_day")
    val notifyEveryDay: Boolean,

    @ColumnInfo(name = "last_fetched_time")
    val lastFetchedTime: Long,

    @ColumnInfo(name = "last_fetched_price")
    val lastFetchedPrice: Float,

    @ColumnInfo(name = "currency_code")
    val currency: String,

    @ColumnInfo(name = "original_price")
    val originalPrice: Float,

    @ColumnInfo(name = "created_on")
    val createdOn: Long = Clock.System.now().toEpochMilliseconds(),
)