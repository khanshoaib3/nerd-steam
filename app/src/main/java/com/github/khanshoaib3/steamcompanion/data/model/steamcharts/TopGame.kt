package com.github.khanshoaib3.steamcompanion.data.model.steamcharts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopGame(
    @PrimaryKey
    val appId: Int,
    val name: String,
    val currentPlayers: Int,
    val peakPlayers: Int,
    val hours: String,
)
