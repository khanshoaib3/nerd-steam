package com.github.khanshoaib3.steamcompanion.data.local.detail

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.PriceAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceAlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(priceAlert: PriceAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(priceAlertList: List<PriceAlert>)

    @Update
    suspend fun update(priceAlert: PriceAlert)

    @Delete
    suspend fun delete(priceAlert: PriceAlert)

    @Query("DELETE FROM price_alerts WHERE appid = :appId")
    suspend fun deleteWithId(appId: Int)

    @Query("DELETE from price_alerts")
    suspend fun deleteAll()

    // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
    @Query("DELETE FROM sqlite_sequence WHERE name = 'price_alerts'")
    suspend fun deletePrimaryKeyIndex()

    @Query("SELECT * from price_alerts WHERE appid = :appId")
    suspend fun getOne(appId: Int): PriceAlert

    @Query("SELECT * from price_alerts")
    fun getAll(): Flow<List<PriceAlert>>

    @Query("SELECT COUNT(*) from price_alerts")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT * from price_alerts WHERE appid = :appId)")
    suspend fun doesExist(appId: Int): Boolean
}
