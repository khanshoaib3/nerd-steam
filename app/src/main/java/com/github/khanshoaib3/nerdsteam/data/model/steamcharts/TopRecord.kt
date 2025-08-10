package com.github.khanshoaib3.nerdsteam.data.model.steamcharts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_records")
data class TopRecord(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,

    @ColumnInfo(name = "appid")
    override val appId: Int,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "peak_players")
    val peakPlayers: Int,

    @ColumnInfo(name = "month")
    val month: String,
) : SteamChartsItem
