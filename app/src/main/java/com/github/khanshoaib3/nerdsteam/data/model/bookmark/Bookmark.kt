package com.github.khanshoaib3.nerdsteam.data.model.bookmark

import android.icu.text.SimpleDateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(tableName = "bookmark")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "appid")
    val appId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long = System.currentTimeMillis() // TODO Change this to LocalDateTime
)

fun Long.formattedTimestamp(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(this)
}
