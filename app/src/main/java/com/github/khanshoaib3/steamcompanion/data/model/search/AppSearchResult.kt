package com.github.khanshoaib3.steamcompanion.data.model.search

import kotlinx.serialization.Serializable

@Serializable
data class AppSearchResult(
    val appid: String,
    val name: String,
    val icon: String,
    val logo: String
)
