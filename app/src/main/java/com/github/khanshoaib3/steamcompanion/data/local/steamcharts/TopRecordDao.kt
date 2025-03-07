package com.github.khanshoaib3.steamcompanion.data.local.steamcharts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TopRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topRecord: TopRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(topRecordsList: List<TopRecord>)

    @Update
    suspend fun update(topRecord: TopRecord)

    @Delete
    suspend fun delete(topRecord: TopRecord)

    @Query("DELETE from top_records")
    suspend fun deleteAll()

    // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
    @Query("DELETE FROM sqlite_sequence WHERE name = 'top_records'")
    fun deletePrimaryKeyIndex()

    @Query("SELECT * from top_records WHERE id = :id")
    fun getOne(id: Int): Flow<TopRecord>

    @Query("SELECT * from top_records")
    fun getAll(): Flow<List<TopRecord>>

    @Query("SELECT COUNT(*) from top_records")
    suspend fun getCount(): Int
}