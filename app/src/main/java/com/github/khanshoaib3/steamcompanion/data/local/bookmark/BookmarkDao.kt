package com.github.khanshoaib3.steamcompanion.data.local.bookmark

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: Bookmark)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(bookmarksList: List<Bookmark>)

    @Update
    suspend fun update(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("DELETE from bookmark WHERE appid = :appId")
    suspend fun deleteByAppId(appId: Int)

    @Query("DELETE from bookmark")
    suspend fun deleteAll()

    // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
    @Query("DELETE FROM sqlite_sequence WHERE name = 'bookmark'")
    fun deletePrimaryKeyIndex()

    @Query("SELECT * from bookmark WHERE appid = :appId")
    fun getOne(appId: Int): Flow<Bookmark>

    @Query("SELECT EXISTS(SELECT * from bookmark WHERE appid = :appId)")
    fun doesExist(appId: Int): Boolean

    @Query("SELECT * from bookmark")
    fun getAll(): Flow<List<Bookmark>>

    @Query("SELECT COUNT(*) from bookmark")
    suspend fun getCount(): Int
}