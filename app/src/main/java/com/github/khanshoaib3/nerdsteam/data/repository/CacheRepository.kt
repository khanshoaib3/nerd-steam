package com.github.khanshoaib3.nerdsteam.data.repository

import android.content.Context
import android.util.Log
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.SerializableAppData
import com.github.khanshoaib3.nerdsteam.utils.runSafeSuspendCatching
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileNotFoundException
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject

private const val TAG = "CacheRepository"

interface CacheRepository {
    suspend fun getCachedData(appId: Int): Result<SerializableAppData>

    suspend fun storeCachedData(serializableAppData: SerializableAppData): Result<Unit>
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
    override suspend fun storeCachedData(serializableAppData: SerializableAppData): Result<Unit> =
        runSafeSuspendCatching {
            val file = getFile(serializableAppData.steamAppId)
            Log.d(TAG, "Caching data (${file.name})...")
            Json.encodeToStream(
                serializer = SerializableAppData.serializer(),
                value = serializableAppData,
                stream = file.outputStream()
            )
        }

    fun getFile(appId: Int): File {
        val date = LocalDate.Format { year();monthNumber();day() }.format(getSteamDay())
        return File(context.cacheDir, "${appId}_${date}.json")
    }

    // Steam refreshes the data at 10am PST, so we use that as the start of the new day
    fun getSteamDay(): LocalDate {
        val nowUtc = Instant.now().atZone(ZoneOffset.UTC)
        val steamDayStartUtc = nowUtc.withHour(18).withMinute(0).withSecond(0).withNano(0)

        return if (nowUtc.isBefore(steamDayStartUtc)) {
            // Still in previous "Steam day"
            nowUtc.minusDays(1).toLocalDate().toKotlinLocalDate()
        } else {
            // Already in today's "Steam day"
            nowUtc.toLocalDate().toKotlinLocalDate()
        }
    }
}
