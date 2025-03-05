package com.github.khanshoaib3.steamcompanion.data.model.steamcharts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_games")
data class TopGame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "appid")
    val appId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "current_players")
    val currentPlayers: Int,

    @ColumnInfo(name = "peak_players")
    val peakPlayers: Int,

    @ColumnInfo(name = "hours_played")
    val hours: Long,
)
