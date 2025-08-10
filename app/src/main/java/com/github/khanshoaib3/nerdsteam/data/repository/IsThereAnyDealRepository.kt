package com.github.khanshoaib3.nerdsteam.data.repository

import android.util.Log
import com.github.khanshoaib3.nerdsteam.data.model.api.GameInfoResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.GameInfoShort
import com.github.khanshoaib3.nerdsteam.data.model.api.PriceInfoResponse
import com.github.khanshoaib3.nerdsteam.data.remote.IsThereAnyDealApiService
import com.github.khanshoaib3.nerdsteam.utils.runSafeSuspendCatching
import javax.inject.Inject

private const val TAG = "IsThereAnyDealRepository"

interface IsThereAnyDealRepository {
    suspend fun lookupGame(appId: Int): Result<GameInfoShort>

    suspend fun getGameInfo(uid: String): Result<GameInfoResponse>

    suspend fun getGameInfoFromAppId(appId: Int): Result<GameInfoResponse>

    suspend fun getPriceInfo(uids: List<String>): Result<List<PriceInfoResponse>>

    suspend fun getPriceInfo(uid: String): Result<PriceInfoResponse>
}

class OnlineIsThereAnyDealRepository @Inject constructor(
    private val isThereAnyDealApiService: IsThereAnyDealApiService,
) : IsThereAnyDealRepository {
    override suspend fun lookupGame(appId: Int): Result<GameInfoShort> =
        runSafeSuspendCatching {
            Log.d(TAG, "Looking for game with appId $appId...")
            val response = isThereAnyDealApiService.lookupGame(appId)
            Log.d(
                TAG, "Game lookup response: id=${response.gameInfoShort?.id} " +
                        "title=${response.gameInfoShort?.title}"
            )
            if (!response.found || response.gameInfoShort == null) throw Exception("Game with appId $appId not found in IsThereAnyDeal.com")
            response.gameInfoShort
        }.onFailure {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::lookupGame", it)
        }

    override suspend fun getGameInfo(uid: String): Result<GameInfoResponse> =
        runSafeSuspendCatching {
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
                getGameInfo(response.id).onSuccess {
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
        runSafeSuspendCatching {
            val response = isThereAnyDealApiService.prices(gameIds = uids, allowVouchers = true)
            response
        }.onFailure {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::getPriceInfo", it)
        }

    override suspend fun getPriceInfo(uid: String): Result<PriceInfoResponse> =
        runSafeSuspendCatching {
            val response = isThereAnyDealApiService.prices(gameIds = listOf(uid), allowVouchers = true)
            response.firstOrNull() ?: throw Exception("Price info for game with uid $uid not found")
        }.onFailure {
            Log.e(TAG, "Error occurred in OnlineIsThereAnyDealRepository::getPriceInfo", it)
        }
}