package com.github.khanshoaib3.nerdsteam.data.model.api

import kotlinx.serialization.Serializable

@Serializable
data class AppSearchResponse(
    val appid: String,
    val name: String,
    val icon: String,
    val logo: String
)