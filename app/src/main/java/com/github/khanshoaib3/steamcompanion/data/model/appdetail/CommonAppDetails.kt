package com.github.khanshoaib3.steamcompanion.data.model.appdetail

import com.github.khanshoaib3.steamcompanion.data.model.api.AppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.model.api.GameInfoResponse
import com.github.khanshoaib3.steamcompanion.data.model.api.Platforms
import com.github.khanshoaib3.steamcompanion.data.model.api.Requirements
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

data class CommonAppDetails(
    val name: String,
    val type: String,
    val imageUrl: String,
    val description: String?,
    val about: String?,
    val releaseDate: String,
    val isReleased: Boolean,
    val developers: List<String>?,
    val publishers: List<String>?,
    val platforms: Platforms,
    val websiteUrl: String?,
    val isFree: Boolean,
    val currentPrice: Float,
    val originalPrice: Float,
    val currency: String,
    val tags: List<String>?,
    val categories: List<Category>?,
    val reviews: List<Reviews>?,
    val requirements: List<Requirement>,
    val media: Media?,
) {
    companion object {
        @Suppress("LocalVariableName")
        internal fun fromIsThereAnyDealAndSteam(
            _steamResponse: AppDetailsResponse?,
            _isThereAnyDealResponse: GameInfoResponse?,
        ): CommonAppDetails {
            val steamResponse = if (_steamResponse?.success == true) _steamResponse.data else null
            val isThereAnyDealResponse = _isThereAnyDealResponse

            val requirements = mutableListOf<Requirement>()

            if (steamResponse?.platforms?.windows == true && steamResponse.pcRequirements is JsonObject) {
                val req = Json.decodeFromJsonElement<Requirements>(steamResponse.pcRequirements)
                requirements.add(
                    Requirement(
                        platform = "Windows",
                        minimumRequirements = req.minimum?.replace("<br>", ""),
                        recommendedRequirements = req.recommended?.replace("<br>", ""),
                    )
                )
            }

            if (steamResponse?.platforms?.mac == true && steamResponse.macRequirements is JsonObject) {
                val req = Json.decodeFromJsonElement<Requirements>(steamResponse.macRequirements)
                requirements.add(
                    Requirement(
                        platform = "MacOS",
                        minimumRequirements = req.minimum?.replace("<br>", ""),
                        recommendedRequirements = req.recommended?.replace("<br>", ""),
                    )
                )
            }

            if (steamResponse?.platforms?.linux == true && steamResponse.linuxRequirements is JsonObject) {
                val req = Json.decodeFromJsonElement<Requirements>(steamResponse.linuxRequirements)
                requirements.add(
                    Requirement(
                        platform = "SteamOS + Linux",
                        minimumRequirements = req.minimum?.replace("<br>", ""),
                        recommendedRequirements = req.recommended?.replace("<br>", ""),
                    )
                )
            }

            val appId = steamResponse?.steamAppId ?: isThereAnyDealResponse?.appid

            return CommonAppDetails(
                name = steamResponse?.name ?: isThereAnyDealResponse?.title ?: "Title",
                type = isThereAnyDealResponse?.type ?: steamResponse?.type ?: "App",
                imageUrl = if (appId != null) "https://cdn.cloudflare.steamstatic.com/steam/apps/$appId/library_600x900.jpg" else "",
                description = steamResponse?.shortDescription,
                about = steamResponse?.aboutTheGame,
                releaseDate = steamResponse?.releaseDate?.date
                    ?: isThereAnyDealResponse?.releaseDate
                    ?: "TBA",
                isReleased = steamResponse?.releaseDate?.comingSoon == false,
                developers = isThereAnyDealResponse?.developers?.map { it.name }
                    ?: steamResponse?.developers,
                publishers = isThereAnyDealResponse?.publishers?.map { it.name }
                    ?: steamResponse?.publishers,
                platforms = steamResponse?.platforms ?: Platforms(
                    windows = true,
                    mac = false,
                    linux = false
                ),
                websiteUrl = steamResponse?.website,
                isFree = steamResponse?.isFree ?: true,
                currentPrice = steamResponse?.priceOverview?.finalPrice?.div(100f) ?: 0f,
                originalPrice = steamResponse?.priceOverview?.initial?.div(100f) ?: 0f,
                currency = steamResponse?.priceOverview?.currency ?: "INR",
                tags = isThereAnyDealResponse?.tags,
                categories = steamResponse?.categories?.map {
                    Category(
                        name = it.description,
                        url = "https://steamdb.info/static/img/categories/${it.id}.png"
                    )
                },
                reviews = isThereAnyDealResponse?.reviews?.map {
                    Reviews(
                        score = it.score ?: 0,
                        name = it.source,
                        url = it.url,
                        count = it.count ?: 0
                    )
                },
                requirements = requirements,
                media = if (steamResponse?.screenshots != null) Media(
                    screenshots = steamResponse.screenshots.map { it.pathFull },
                    movies = steamResponse.movies?.map { it.webm.low }
                ) else null
            )
        }
    }
}


data class Category(
    val name: String,
    val url: String,
)

data class Requirement(
    val platform: String,
    val minimumRequirements: String?,
    val recommendedRequirements: String?,
)

data class Media(
    val screenshots: List<String>,
    val movies: List<String>?,
)

data class Reviews(
    val score: Int,
    val name: String,
    val url: String,
    val count: Int,
)
