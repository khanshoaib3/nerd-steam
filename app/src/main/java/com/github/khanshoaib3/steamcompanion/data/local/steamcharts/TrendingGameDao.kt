package com.github.khanshoaib3.steamcompanion.data.local.steamcharts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trendingGame: TrendingGame)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(trendingGamesList: List<TrendingGame>)

    @Update
    suspend fun update(trendingGame: TrendingGame)

    @Delete
    suspend fun delete(trendingGame: TrendingGame)

    @Query("DELETE from trending_games")
    suspend fun deleteAll()

    // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
    @Query("DELETE FROM sqlite_sequence WHERE name = 'trending_games'")
    fun deletePrimaryKeyIndex()

    @Query("SELECT * from trending_games WHERE id = :id")
    fun getOne(id: Int): Flow<TrendingGame>

    @Query("SELECT * from trending_games")
    fun getAll(): Flow<List<TrendingGame>>

    @Query("SELECT COUNT(*) from trending_games")
    suspend fun getCount(): Int
}