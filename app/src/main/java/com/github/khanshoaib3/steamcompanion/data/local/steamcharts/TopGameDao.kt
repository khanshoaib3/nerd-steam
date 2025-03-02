package com.github.khanshoaib3.steamcompanion.data.local.steamcharts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import kotlinx.coroutines.flow.Flow

@Dao
interface TopGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topGame: TopGame)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(topGamesList: List<TopGame>)

    @Update
    suspend fun update(topGame: TopGame)

    @Delete
    suspend fun delete(topGame: TopGame)

    @Query("DELETE from top_games")
    suspend fun deleteAll()

    @Query("SELECT * from top_games WHERE id = :id")
    fun getOne(id: Int): Flow<TopGame>

    @Query("SELECT * from top_games")
    fun getAll(): Flow<List<TopGame>>

    @Query("SELECT COUNT(*) from top_games")
    suspend fun getCount(): Int
}