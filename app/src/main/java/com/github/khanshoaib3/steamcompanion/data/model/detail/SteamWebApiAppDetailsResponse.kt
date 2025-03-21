package com.github.khanshoaib3.steamcompanion.data.model.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamWebApiAppDetailsResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: AppDetail? = null
)

@Serializable
data class AppDetail(
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("steam_appid") val steamAppId: Int,
    @SerialName("required_age") val requiredAge: Int,
    @SerialName("is_free") val isFree: Boolean,
    @SerialName("controller_support") val controllerSupport: String? = null,
//    @SerialName("dlc") val dlc: List<Int>? = null,
    @SerialName("detailed_description") val detailedDescription: String,
    @SerialName("about_the_game") val aboutTheGame: String,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("supported_languages") val supportedLanguages: String,
    @SerialName("header_image") val headerImage: String,
    @SerialName("capsule_image") val capsuleImage: String,
    @SerialName("website") val website: String? = null,
    @SerialName("developers") val developers: List<String>? = null,
    @SerialName("publishers") val publishers: List<String>? = null,
    @SerialName("platforms") val platforms: Platforms,
//    @SerialName("categories") val categories: List<Category>,
//    @SerialName("genres") val genres: List<Genre>,
//    @SerialName("metacritic") val metacritic: Metacritic? = null,
//    @SerialName("recommendations") val recommendations: Recommendations? = null,
//    @SerialName("release_date") val releaseDate: ReleaseDate,
//    @SerialName("pc_requirements") val pcRequirements: JsonElement? = null,
//    @SerialName("mac_requirements") val macRequirements: JsonElement? = null,
//    @SerialName("linux_requirements") val linuxRequirements: JsonElement? = null,
//    @SerialName("price_overview") val priceOverview: PriceOverview? = null,
//    @SerialName("screenshots") val screenshots: List<Screenshot>? = null,
//    @SerialName("packages") val packages: List<Int>? = null,
//    @SerialName("package_groups") val packageGroups: List<PackageGroup>? = null
)

@Serializable
data class Platforms(
    @SerialName("windows") val windows: Boolean,
    @SerialName("mac") val mac: Boolean,
    @SerialName("linux") val linux: Boolean
)

@Serializable
data class Category(
    @SerialName("id") val id: Int,
    @SerialName("description") val description: String
)

@Serializable
data class Genre(
    @SerialName("id") val id: String,
    @SerialName("description") val description: String
)

@Serializable
data class Metacritic(
    @SerialName("score") val score: Int,
    @SerialName("url") val url: String
)

@Serializable
data class Recommendations(
    @SerialName("total") val total: Int
)

@Serializable
data class ReleaseDate(
    @SerialName("coming_soon") val comingSoon: Boolean,
    @SerialName("date") val date: String
)

@Serializable
data class Requirements(
    @SerialName("minimum") val minimum: String? = null,
    @SerialName("recommended") val recommended: String? = null
)

@Serializable
data class PriceOverview(
    @SerialName("currency") val currency: String,
    @SerialName("initial") val initial: Int,
    @SerialName("final") val finalPrice: Int,
    @SerialName("discount_percent") val discountPercent: Int
)

@Serializable
data class Screenshot(
    @SerialName("id") val id: Int,
    @SerialName("path_thumbnail") val pathThumbnail: String,
    @SerialName("path_full") val pathFull: String
)

@Serializable
data class PackageGroup(
    @SerialName("name") val name: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("selection_text") val selectionText: String,
    @SerialName("save_text") val saveText: String? = null,
    @SerialName("display_type") val displayType: Int,
    @SerialName("is_recurring_subscription") val isRecurringSubscription: String,
    @SerialName("subs") val subs: List<Sub>
)

@Serializable
data class Sub(
    @SerialName("packageid") val packageId: Int,
    @SerialName("percent_savings_text") val percentSavingsText: String,
    @SerialName("percent_savings") val percentSavings: Int,
    @SerialName("option_text") val optionText: String,
    @SerialName("option_description") val optionDescription: String,
    @SerialName("can_get_free_license") val canGetFreeLicense: String,
    @SerialName("is_free_license") val isFreeLicense: Boolean,
    @SerialName("price_in_cents_with_discount") val priceInCentsWithDiscount: Int
)
