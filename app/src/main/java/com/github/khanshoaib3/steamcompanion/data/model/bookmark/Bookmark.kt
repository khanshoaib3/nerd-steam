package com.github.khanshoaib3.steamcompanion.data.model.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "appid")
    val appId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long = System.currentTimeMillis()
)
