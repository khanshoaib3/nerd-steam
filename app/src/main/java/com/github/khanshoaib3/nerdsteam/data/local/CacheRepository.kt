package com.github.khanshoaib3.nerdsteam.data.local

import android.content.Context
import android.util.Log
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.SerializableAppData
import com.github.khanshoaib3.nerdsteam.utils.DateTimeUtils
import com.github.khanshoaib3.nerdsteam.utils.runSafeSuspendCatching
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

private const val TAG = "CacheRepository"

interface CacheRepository {
    suspend fun getCachedData(appId: Int): Result<SerializableAppData>

    suspend fun storeCachedData(appId: Int, serializableAppData: SerializableAppData): Result<Unit>
}

class LocalCacheRepository @Inject constructor(
    private val context: Context,
) : CacheRepository {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getCachedData(appId: Int): Result<SerializableAppData> =
        runSafeSuspendCatching {
            val file = getFile(appId)
            if (!file.exists()) throw FileNotFoundException()
            Log.d(TAG, "Fetching data from cache (${file.name})...")

            Json.decodeFromStream<SerializableAppData>(file.inputStream())
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun storeCachedData(appId: Int, serializableAppData: SerializableAppData): Result<Unit> =
        runSafeSuspendCatching {
            val file = getFile(appId)
            Log.d(TAG, "Caching data (${file.name})...")
            Json.encodeToStream(
                serializer = SerializableAppData.serializer(),
                value = serializableAppData,
                stream = file.outputStream()
            )
        }

    fun getFile(appId: Int): File {
        val date = LocalDate.Format { year();monthNumber();day() }.format(DateTimeUtils.getSteamDay())
        return File(context.cacheDir, "${appId}_${date}.json")
    }
}
