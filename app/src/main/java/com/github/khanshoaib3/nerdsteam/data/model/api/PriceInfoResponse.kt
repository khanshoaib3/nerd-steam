package com.github.khanshoaib3.nerdsteam.data.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceInfoResponse(
    val id: String,
    val historyLow: HistoryLow,
    @SerialName("deals") val priceDeals: List<PriceDeal>
)

@Serializable
data class HistoryLow(
    val all: Price?,
    val y1: Price?,
    val m3: Price?
)

@Serializable
data class Price(
    val amount: Float,
    val amountInt: Int,
    val currency: String
)

@Serializable
data class PriceDeal(
    val shop: Shop,
    val price: Price,
    val regular: Price,
    val cut: Int,
    val voucher: String?, // nullable
    val storeLow: Price?,
    val flag: String?, // nullable
    val drm: List<Drm>?,
    val platforms: List<Platform>,
    val timestamp: String,
    val expiry: String?, // nullable
    val url: String
)

@Serializable
data class Shop(
    val id: Int,
    val name: String
)

@Serializable
data class Drm(
    val id: Int,
    val name: String
)

@Serializable
data class Platform(
    val id: Int,
    val name: String
)
