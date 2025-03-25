package com.github.khanshoaib3.steamcompanion.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.github.khanshoaib3.steamcompanion.R
import kotlinx.coroutines.launch


private fun WindowSizeClass.isCompact() =
    windowWidthSizeClass == WindowWidthSizeClass.COMPACT ||
            windowHeightSizeClass == WindowHeightSizeClass.COMPACT

class SteamCompanionNavSuiteScope(
    val navSuiteType: NavigationSuiteType,
    val drawerState: DrawerState
)

@Composable
fun SteamCompanionNavigationWrapper(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (SteamCompanionTopLevelRoute) -> Unit,
    content: @Composable SteamCompanionNavSuiteScope.() -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }

    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.isCompact() -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
                windowSize.width >= 1200.dp -> NavigationSuiteType.NavigationDrawer

        else -> NavigationSuiteType.NavigationRail
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    // Avoid opening the modal drawer when there is a permanent drawer or a bottom nav bar,
    // but always allow closing an open drawer.
    val gesturesEnabled = drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationRail

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                                contentDescription = "Close drawer"
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TOP_LEVEL_ROUTES.forEach { topLevelRoute ->
                        val isSelected =
                            currentDestination?.hasRoute(topLevelRoute.route::class) == true
                        NavigationDrawerItem(
                            selected = isSelected,
                            label = {
                                Text(
                                    text = topLevelRoute.name,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.icon,
                                    contentDescription = topLevelRoute.name
                                )
                            },
//                            colors = NavigationDrawerItemDefaults.colors(
//                                unselectedContainerColor = Color.Transparent
//                            ),
                            onClick = { navigateToTopLevelDestination(topLevelRoute) }
                        )
                    }
                }
            }
        }
    ) {
        NavigationSuiteScaffold(
            layoutType = navLayoutType,
            navigationSuiteItems = {
                TOP_LEVEL_ROUTES.forEach { topLevelRoute ->
                    val isSelected =
                        currentDestination?.hasRoute(topLevelRoute.route::class) == true
                    item(
                        selected = isSelected,
                        label = {
                            Text(
                                text = topLevelRoute.name,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.icon,
                                contentDescription = topLevelRoute.name
                            )
                        },
//                            colors = NavigationDrawerItemDefaults.colors(
//                                unselectedContainerColor = Color.Transparent
//                            ),
                        onClick = { navigateToTopLevelDestination(topLevelRoute) }
                    )
                }
            }
        )
        {
            SteamCompanionNavSuiteScope(navLayoutType, drawerState).content()
        }
    }
}