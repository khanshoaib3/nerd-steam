package com.github.khanshoaib3.steamcompanion.data.local

import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class Converters {

    @ExperimentalTime
    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    @ExperimentalTime
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }
}