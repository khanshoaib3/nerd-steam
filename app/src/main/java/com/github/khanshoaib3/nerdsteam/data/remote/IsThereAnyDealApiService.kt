package com.github.khanshoaib3.nerdsteam.data.remote

import androidx.annotation.IntRange
import com.github.khanshoaib3.nerdsteam.BuildConfig
import com.github.khanshoaib3.nerdsteam.data.model.api.GameInfoResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.LookupGameResponse
import com.github.khanshoaib3.nerdsteam.data.model.api.PriceInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Ref: https://docs.isthereanydeal.com/
interface IsThereAnyDealApiService {
    @GET("/games/lookup/v1")
    suspend fun lookupGame(
        @Query("appid") appId: Int? = null,
        @Query("title") title: String? = null,
        @Query("key") apiKey: String = BuildConfig.IS_THERE_ANY_DEAL_API_KEY,
    ): LookupGameResponse

    @GET("/games/info/v2")
    suspend fun gameInfo(
        @Query("id") id: String,
        @Query("key") apiKey: String = BuildConfig.IS_THERE_ANY_DEAL_API_KEY,
    ): GameInfoResponse

    // https://docs.isthereanydeal.com/#tag/Prices/operation/games-prices-v3
    @POST("/games/prices/v3")
    suspend fun prices(
        @Body gameIds: List<String>,
        @Query("country") countryCode: String = "in",
        @Query("deals") getOnlyDeals: Boolean? = null,
        @Query("vouchers") allowVouchers: Boolean? = null,
        @Query("capacity") @IntRange(from = 0) capacity: Int? = null,
        @Query("shops") shopIds: List<Int>? = null,
        @Query("key") apiKey: String = BuildConfig.IS_THERE_ANY_DEAL_API_KEY,
    ): List<PriceInfoResponse>
}