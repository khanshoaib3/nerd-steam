package com.github.khanshoaib3.nerdsteam.data.model.steamcharts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending_games")
data class TrendingGame(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,

    @ColumnInfo(name = "appid")
    override val appId: Int,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "gain")
    val gain: String,

    @ColumnInfo(name = "current_players")
    val currentPlayers: Int,
) : SteamChartsItem