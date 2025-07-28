package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal.GameInfoResponse
import com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal.LookupGameResponse
import com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal.PriceInfoResponse
import com.github.khanshoaib3.steamcompanion.data.remote.IsThereAnyDealApiService
import java.lang.Exception
import javax.inject.Inject
import kotlin.jvm.Throws

private const val TAG = "IsThereAnyDealRepository"

interface IsThereAnyDealRepository {
    @Throws
    suspend fun lookupGame(appId: Int): LookupGameResponse

    suspend fun getGameInfo(uid: String): GameInfoResponse

    suspend fun getGameInfoFromAppId(appId: Int): GameInfoResponse

    suspend fun getPriceInfo(uids: List<String>): List<PriceInfoResponse>

    suspend fun getPriceInfo(uid: String): List<PriceInfoResponse>
}

class OnlineIsThereAnyDealRepository @Inject constructor(
    private val isThereAnyDealApiService: IsThereAnyDealApiService,
) : IsThereAnyDealRepository {
    override suspend fun lookupGame(appId: Int): LookupGameResponse {
        try {
            Log.d(TAG, "Looking for game with appId $appId...")
            val response = isThereAnyDealApiService.lookupGame(appId)
            Log.d(TAG, "Game lookup response: id=${response.gameInfoShort?.id}, title=${response.gameInfoShort?.title}")
            return response
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::lookupGame")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getGameInfo(uid: String): GameInfoResponse {
        try {
            Log.d(TAG, "Looking for game with id $uid")
            val response = isThereAnyDealApiService.gameInfo(uid)
            Log.d(TAG, "Game Info response: id=${response.id}, title=${response.title}")
            return response
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::getGameInfo")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getGameInfoFromAppId(appId: Int): GameInfoResponse {
        val lookupGame = lookupGame(appId = appId)
        if (!lookupGame.found || lookupGame.gameInfoShort == null) throw Error("Game with appId $appId not found")
        val gameInfo = getGameInfo(lookupGame.gameInfoShort.id)
        return gameInfo
    }

    override suspend fun getPriceInfo(uids: List<String>): List<PriceInfoResponse> {
        try {
            val response = isThereAnyDealApiService.prices(gameIds = uids)
            return response
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::getPriceInfo")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getPriceInfo(uid: String) = getPriceInfo(listOf(uid))

}