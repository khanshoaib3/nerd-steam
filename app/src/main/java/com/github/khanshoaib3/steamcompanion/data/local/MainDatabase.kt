package com.github.khanshoaib3.steamcompanion.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.khanshoaib3.steamcompanion.data.local.bookmark.BookmarkDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopGameDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TopRecordDao
import com.github.khanshoaib3.steamcompanion.data.local.steamcharts.TrendingGameDao
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame

@Database(
    entities = [TrendingGame::class, TopGame::class, TopRecord::class, Bookmark::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun trendingGameDao(): TrendingGameDao
    abstract fun topGameDao(): TopGameDao
    abstract fun topRecordDao(): TopRecordDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var Instance: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MainDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}