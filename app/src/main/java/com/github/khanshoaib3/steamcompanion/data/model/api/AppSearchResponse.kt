package com.github.khanshoaib3.steamcompanion.data.model.api

import kotlinx.serialization.Serializable

@Serializable
data class AppSearchResponse(
    val appid: String,
    val name: String,
    val icon: String,
    val logo: String
)