package com.github.khanshoaib3.steamcompanion.data.model.steamcharts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopRecord(
    @PrimaryKey
    val appId: Int,
    val name: String,
    val peakPlayers: Int,
    val time: String,
)
