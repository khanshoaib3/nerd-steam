package com.github.khanshoaib3.steamcompanion.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route.AppDetail
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route.Home
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route.Search
import kotlinx.serialization.Serializable

data class SteamCompanionTopLevelRoute(
    val name: String,
    val route: Route,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Search : Route

    @Serializable
    data class AppDetail(val appId: Int?) : Route

    @Serializable
    data object Bookmark : Route
//    @Serializable data object DirectMessages : Route
//    @Serializable data object Groups : Route
}

class SteamCompanionNavigationActions(private val navController: NavHostController) {
    fun navigateToTopLevelRoute(destination: SteamCompanionTopLevelRoute) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToAppDetailsScreen(appId: Int?) {
        navController.navigate(AppDetail(appId))
    }
}

val NAV_BAR_ROUTES = listOf(
    SteamCompanionTopLevelRoute(
        name = "Home",
        route = Home,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    SteamCompanionTopLevelRoute(
        name = "Search",
        route = Search,
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search
    ),
)
val NAV_DRAWER_ROUTES = listOf(
    SteamCompanionTopLevelRoute(
        name = "Home",
        route = Home,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    SteamCompanionTopLevelRoute(
        name = "Search",
        route = Search,
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search
    ),
    SteamCompanionTopLevelRoute(
        name = "Bookmark",
        route = Route.Bookmark,
        icon = Icons.Outlined.BookmarkBorder,
        selectedIcon = Icons.Filled.Bookmark
    )
)
