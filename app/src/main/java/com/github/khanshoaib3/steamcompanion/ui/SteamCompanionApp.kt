package com.github.khanshoaib3.steamcompanion.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.khanshoaib3.steamcompanion.ui.screen.home.HomeScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.search.SearchScreen
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteamCompanionApp(modifier: Modifier = Modifier) {
    // https://developer.android.com/develop/ui/compose/components/app-bars#center
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navController = rememberNavController()
    val topLevelRoutes = listOf(
        TopLevelRoute<Any>(
            name = "Home",
            route = HomeRoute,
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        TopLevelRoute<Any>(
            name = "Search",
            route = SearchRoute,
            icon = Icons.Outlined.Search,
            selectedIcon = Icons.Filled.Search
        )
    )

    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = { TopBar(topAppBarScrollBehavior) },
        bottomBar = { NavBar(navController = navController, topLevelRoutes = topLevelRoutes) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = modifier.padding(innerPadding)
        ) {
            composable<HomeRoute>(
                enterTransition = {
                    // https://developer.android.com/develop/ui/compose/animation/quick-guide#animate-whilst
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
                exitTransition = {
                    // https://developer.android.com/develop/ui/compose/animation/quick-guide#animate-whilst
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
            ) { HomeScreen() }
            composable<SearchRoute>(
                enterTransition = {
                    // https://developer.android.com/develop/ui/compose/animation/quick-guide#animate-whilst
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    // https://developer.android.com/develop/ui/compose/animation/quick-guide#animate-whilst
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
            ) { SearchScreen() }
        }
    }
}

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

@Serializable
object HomeRoute

@Serializable
object SearchRoute

@Preview
@Composable
private fun SteamCompanionAppPreview() {
    SteamCompanionTheme {
        SteamCompanionApp()
    }
}