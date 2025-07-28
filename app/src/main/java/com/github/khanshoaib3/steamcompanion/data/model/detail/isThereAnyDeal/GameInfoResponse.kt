package com.github.khanshoaib3.steamcompanion.data.model.detail.isThereAnyDeal

import kotlinx.serialization.Serializable

@Serializable
data class GameInfoResponse(
    val id: String,
    val slug: String,
    val title: String,
    val type: String,
    val mature: Boolean,
    val assets: GameAssets,
    val earlyAccess: Boolean,
    val achievements: Boolean,
    val tradingCards: Boolean,
    val appid: Int,
    val tags: List<String>,
    val releaseDate: String,
    val developers: List<Company>,
    val publishers: List<Company>,
    val reviews: List<Review>,
    val stats: Stats,
    val players: Players,
    val urls: Urls
)

@Serializable
data class Company(
    val id: Int,
    val name: String
)

@Serializable
data class Review(
    val score: Int,
    val source: String,
    val count: Int,
    val url: String
)

@Serializable
data class Stats(
    val rank: Int,
    val waitlisted: Int,
    val collected: Int
)

@Serializable
data class Players(
    val recent: Int,
    val day: Int,
    val week: Int,
    val peak: Int
)

@Serializable
data class Urls(
    val game: String
)
