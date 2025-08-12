package com.github.khanshoaib3.nerdsteam.data.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Ref: https://docs.isthereanydeal.com/#tag/Game/operation/games-lookup-v1
@Serializable
data class LookupGameResponse(
    val found: Boolean,
    @SerialName("game") val gameInfoShortResponse: GameInfoShortResponse? = null
)

@Serializable
data class GameInfoShortResponse(
    val id: String,
    val slug: String,
    val title: String,
    val type: String,
    val mature: Boolean,
    val assets: GameAssets
)

@Serializable
data class GameAssets(
    val boxart: String,
    val banner145: String,
    val banner300: String,
    val banner400: String,
    val banner600: String
)
