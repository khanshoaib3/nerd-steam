package com.github.khanshoaib3.steamcompanion.data.repository

import android.util.Log
import com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal.GameInfoResponse
import com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal.LookupGameResponse
import com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal.PriceInfoResponse
import com.github.khanshoaib3.steamcompanion.data.remote.IsThereAnyDealApiService
import javax.inject.Inject

private const val TAG = "IsThereAnyDealRepository"

interface IsThereAnyDealRepository {
    suspend fun lookupGame(appId: Int): Result<LookupGameResponse>

    suspend fun getGameInfo(uid: String): Result<GameInfoResponse>

    suspend fun getGameInfoFromAppId(appId: Int): Result<GameInfoResponse>

    suspend fun getPriceInfo(uids: List<String>): Result<List<PriceInfoResponse>>

    suspend fun getPriceInfo(uid: String): Result<List<PriceInfoResponse>>
}

class OnlineIsThereAnyDealRepository @Inject constructor(
    private val isThereAnyDealApiService: IsThereAnyDealApiService,
) : IsThereAnyDealRepository {
    override suspend fun lookupGame(appId: Int): Result<LookupGameResponse> =
        runCatching {
            Log.d(TAG, "Looking for game with appId $appId...")
            val response = isThereAnyDealApiService.lookupGame(appId)
            Log.d(
                TAG,
                "Game lookup response: id=${response.gameInfoShort?.id}, title=${response.gameInfoShort?.title}"
            )
            response
        }.onFailure {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::lookupGame", it)
        }

    override suspend fun getGameInfo(uid: String): Result<GameInfoResponse> =
        runCatching {
            Log.d(TAG, "Looking for game with id $uid")
            val response = isThereAnyDealApiService.gameInfo(uid)
            Log.d(TAG, "Game Info response: id=${response.id}, title=${response.title}")
            response
        }.onFailure {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::getGameInfo", it)
        }

    override suspend fun getGameInfoFromAppId(appId: Int): Result<GameInfoResponse> {
        lookupGame(appId = appId).fold(
            onSuccess = { response ->
                if (!response.found || response.gameInfoShort == null) return Result.failure(
                    Exception("Game with appId $appId not found")
                )
                getGameInfo(response.gameInfoShort.id).onSuccess {
                    return Result.success(it)
                }
            },
            onFailure = { exception ->
                return Result.failure(exception)
            }
        )
        return Result.failure(Exception("Unknown error occurred")) // Should not be reached
    }

    override suspend fun getPriceInfo(uids: List<String>): Result<List<PriceInfoResponse>> =
        runCatching {
            val response = isThereAnyDealApiService.prices(gameIds = uids)
            response
        }.onFailure {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::getPriceInfo", it)
        }

    override suspend fun getPriceInfo(uid: String) = getPriceInfo(listOf(uid))
}