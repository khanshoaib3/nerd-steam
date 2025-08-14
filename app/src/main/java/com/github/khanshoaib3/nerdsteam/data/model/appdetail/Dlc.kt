package com.github.khanshoaib3.nerdsteam.data.model.appdetail

import com.github.khanshoaib3.nerdsteam.data.model.api.AppDetailDataResponse
import kotlinx.serialization.Serializable

@Serializable
data class Dlc(
    val appId: Int,
    val name: String,
    val price: Double?,
    val currencyCode: String?,
    val imageUrl: String,
    val steamUrl: String,
)

fun AppDetailDataResponse.toDlc() = Dlc(
    appId = this.steamAppId,
    name = this.name,
    price = this.priceOverview?.finalPrice?.div(100.0),
    currencyCode = this.priceOverview?.currency,
    imageUrl = this.capsuleImage,
    steamUrl = "https://store.steampowered.com/app/${this.steamAppId}/",
)
