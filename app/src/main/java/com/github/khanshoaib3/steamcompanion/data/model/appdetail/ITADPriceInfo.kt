package com.github.khanshoaib3.steamcompanion.data.model.appdetail

import com.github.khanshoaib3.steamcompanion.data.model.api.PriceInfoResponse

data class ITADPriceInfo(
    val currency: String?,
    val historicLow: Float?,
    val oneYearLow: Float?,
    val threeMonthsLow: Float?,
    val deals: List<ITADPriceDealsInfo> = listOf(),
)

data class ITADPriceDealsInfo(
    val shopName: String,
    val url: String,
    val price: Float,
    val regularPrice: Float,
    val currency: String,
    val discountPercentage: Int,
    val voucher: String?,
    val timeStamp: String,
    val expiry: String?,
)

fun PriceInfoResponse.toITADPriceInfo() =
    ITADPriceInfo(
        currency = historyLow.all?.currency,
        historicLow = historyLow.all?.amount,
        oneYearLow = historyLow.y1?.amount,
        threeMonthsLow = historyLow.m3?.amount,
        deals = priceDeals.map { deal ->
            ITADPriceDealsInfo(
                shopName = deal.shop.name,
                url = deal.url,
                price = deal.price.amount,
                regularPrice = deal.regular.amount,
                currency = deal.regular.currency,
                discountPercentage = deal.cut,
                voucher = deal.voucher,
                timeStamp = deal.timestamp,
                expiry = deal.expiry
            )
        }
    )

