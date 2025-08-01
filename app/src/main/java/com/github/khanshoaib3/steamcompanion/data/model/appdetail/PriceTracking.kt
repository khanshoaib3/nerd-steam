package com.github.khanshoaib3.steamcompanion.data.model.appdetail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "price_tracking")
data class PriceTracking(
    @ColumnInfo(name = "appid")
    @PrimaryKey val appId: Int,

    @ColumnInfo(name = "game_name")
    val gameName: String,

    @ColumnInfo(name = "target_price")
    val targetPrice: Float,

    @ColumnInfo(name = "notify_every_day")
    val notifyEveryDay: Boolean
)