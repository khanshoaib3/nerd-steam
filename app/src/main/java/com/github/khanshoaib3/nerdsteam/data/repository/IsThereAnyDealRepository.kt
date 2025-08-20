package com.github.khanshoaib3.nerdsteam.data.repository

import com.github.khanshoaib3.nerdsteam.data.model.api.GameInfoResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.GameInfoShortResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.PriceInfoResponse
import com.github.khanshoaib3.nerdsteam.data.remote.IsThereAnyDealApiService
import com.github.khanshoaib3.nerdsteam.utils.runSafeSuspendCatching
import timber.log.Timber
import javax.inject.Inject

interface IsThereAnyDealRepository {
    suspend fun lookupGame(appId: Int): Result<GameInfoShortResponse>

    suspend fun getGameInfo(uid: String): Result<GameInfoResponse>

    suspend fun getGameInfoFromAppId(appId: Int): Result<GameInfoResponse>

    suspend fun getPriceInfo(uids: List<String>): Result<List<PriceInfoResponse>>

    suspend fun getPriceInfo(uid: String): Result<PriceInfoResponse>
}

class OnlineIsThereAnyDealRepository @Inject constructor(
    private val isThereAnyDealApiService: IsThereAnyDealApiService,
) : IsThereAnyDealRepository {
    override suspend fun lookupGame(appId: Int): Result<GameInfoShortResponse> =
        runSafeSuspendCatching {
            Timber.d("Looking for game with appId $appId...")
            val response = isThereAnyDealApiService.lookupGame(appId)
            Timber.d("Game lookup response: id=${response.gameInfoShortResponse?.id} title=${response.gameInfoShortResponse?.title}")
            if (!response.found || response.gameInfoShortResponse == null) throw Exception("Game with appId $appId not found in IsThereAnyDeal.com")
            response.gameInfoShortResponse
        }.onFailure {
            Timber.e("Error occurred in OnlineIsThereAnyDealRepository::lookupGame")
            Timber.e(it)
        }

    override suspend fun getGameInfo(uid: String): Result<GameInfoResponse> =
        runSafeSuspendCatching {
            Timber.d("Looking for game with id $uid")
            val response = isThereAnyDealApiService.gameInfo(uid)
            Timber.d("Game Info response: id=${response.id}, title=${response.title}")
            response
        }.onFailure {
            Timber.e("Error occurred in OnlineIsThereAnyDealRepository::getGameInfo")
            Timber.e(it)
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
            Timber.e("Error occurred in OnlineIsThereAnyDealRepository::getPriceInfo")
            Timber.e(it)
        }

    override suspend fun getPriceInfo(uid: String): Result<PriceInfoResponse> =
        runSafeSuspendCatching {
            val response =
                isThereAnyDealApiService.prices(gameIds = listOf(uid), allowVouchers = true)
            response.firstOrNull() ?: throw Exception("Price info for game with uid $uid not found")
        }.onFailure {
            Timber.e("Error occurred in OnlineIsThereAnyDealRepository::getPriceInfo")
            Timber.e(it)
        }
}