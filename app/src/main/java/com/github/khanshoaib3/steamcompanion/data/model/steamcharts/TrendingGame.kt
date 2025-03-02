package com.github.khanshoaib3.steamcompanion.data.model.steamcharts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrendingGame(
    @PrimaryKey(autoGenerate = false)
    val appId: Int,
    val name: String,
    val gain: String,
    val currentPlayers: Int,
)
