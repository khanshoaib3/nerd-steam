package com.github.khanshoaib3.steamcompanion.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route.Home
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route.Search
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    data object Home : Route {
        override val name = "Home"
        override val icon = Icons.Outlined.Home
        override val selectedIcon = Icons.Filled.Home
        override val isTopLevel = true
    }

    @Serializable
    data object Search : Route {
        override val name = "Search"
        override val icon = Icons.Outlined.Search
        override val selectedIcon = Icons.Filled.Search
        override val isTopLevel = true
    }

    @Serializable
    data class AppDetail(val appId: Int?) : Route {
        override val name = "Home"
        override val icon = null
        override val selectedIcon = null
        override val isTopLevel = false
    }

    @Serializable
    data object Bookmark : Route {
        override val name = "Bookmark"
        override val icon = Icons.Outlined.BookmarkBorder
        override val selectedIcon = Icons.Filled.Bookmark
        override val isTopLevel = false
    }

    val name: String
    val icon: ImageVector?
    val selectedIcon: ImageVector?
    val isTopLevel: Boolean
}

val NAV_BAR_ROUTES = listOf(Home, Search)
val NAV_RAIL_ROUTES = listOf(Home, Search, Route.Bookmark)
