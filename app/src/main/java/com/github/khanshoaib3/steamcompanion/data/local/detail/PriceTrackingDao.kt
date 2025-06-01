package com.github.khanshoaib3.steamcompanion.data.local.detail

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.khanshoaib3.steamcompanion.data.model.detail.PriceTracking
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceTrackingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(priceTracking: PriceTracking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(priceTrackingList: List<PriceTracking>)

    @Update
    suspend fun update(priceTracking: PriceTracking)

    @Delete
    suspend fun delete(priceTracking: PriceTracking)

    @Query("DELETE FROM price_tracking WHERE appid = :appId")
    suspend fun deleteWithId(appId: Int)

    @Query("DELETE from top_games")
    suspend fun deleteAll()

    // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
    @Query("DELETE FROM sqlite_sequence WHERE name = 'price_tracking'")
    suspend fun deletePrimaryKeyIndex()

    @Query("SELECT * from price_tracking WHERE appid = :appId")
    suspend fun getOne(appId: Int): PriceTracking

    @Query("SELECT * from price_tracking")
    fun getAll(): Flow<List<PriceTracking>>

    @Query("SELECT COUNT(*) from price_tracking")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT * from price_tracking WHERE appid = :appId)")
    suspend fun doesExist(appId: Int): Boolean
}
