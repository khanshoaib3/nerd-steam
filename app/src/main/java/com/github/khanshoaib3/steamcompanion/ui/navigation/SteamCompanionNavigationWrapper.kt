package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.github.khanshoaib3.steamcompanion.R
import kotlinx.coroutines.launch

class SteamCompanionNavSuiteScope(
    val navSuiteType: NavigationSuiteType,
    val drawerState: DrawerState,
)

@Composable
fun SteamCompanionNavigationWrapper(
    backStack: SnapshotStateList<Any>,
    navigateTo: (Route) -> Unit,
    content: @Composable (SteamCompanionNavSuiteScope.() -> Unit),
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
            -> NavigationSuiteType.NavigationRail

        else -> NavigationSuiteType.NavigationBar
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    if (backStack.lastOrNull() == Route.Bookmark) {
        SteamCompanionNavSuiteScope(navLayoutType, drawerState).content()
        return
    }

    val coroutineScope = rememberCoroutineScope()
    // Avoid opening the modal drawer when there is a permanent drawer or a bottom nav bar,
    // but always allow closing an open drawer.
    val gesturesEnabled =
        drawerState.isOpen || navLayoutType != NavigationSuiteType.NavigationDrawer

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalNavigationDrawerContent(
                backStack = backStack,
                navigateToTopLevelDestination = navigateTo,
                onDrawerClicked = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        NavigationSuiteScaffoldLayout(
            layoutType = navLayoutType,
            navigationSuite = {
                when (navLayoutType) {
                    NavigationSuiteType.NavigationBar -> SteamCompanionNavBar(
                        backStack = backStack,
                        navigateToTopLevelDestination = navigateTo
                    )

                    NavigationSuiteType.NavigationRail -> SteamCompanionNavRail(
                        backStack = backStack,
                        navigateToTopLevelDestination = navigateTo,
                        onDrawerClicked = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
            }
        )
        {
            SteamCompanionNavSuiteScope(navLayoutType, drawerState).content()
        }
    }
}

@Composable
fun SteamCompanionNavRail(
    backStack: SnapshotStateList<Any>,
    navigateToTopLevelDestination: (Route) -> Unit,
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            NavigationRailItem(
                selected = false,
                onClick = onDrawerClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open app drawer"
                    )
                }
            )
            Spacer(Modifier.height(8.dp)) // NavigationRailHeaderPadding
            Spacer(Modifier.height(4.dp)) // NavigationRailVerticalPadding
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NAV_BAR_ROUTES.forEach { topLevelRoute ->
                NavigationRailItem(
                    selected = backStack.lastOrNull() == topLevelRoute,
                    onClick = { navigateToTopLevelDestination(topLevelRoute) },
                    icon = {
                        Icon(
                            imageVector = topLevelRoute.selectedIcon?: Icons.Default.QuestionMark,
                            contentDescription = topLevelRoute.name
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SteamCompanionNavBar(
    backStack: SnapshotStateList<Any>,
    navigateToTopLevelDestination: (Route) -> Unit,
) {
    val density = LocalDensity.current
    val view = LocalView.current
    val customWindowInsets = WindowInsets(
        top = 0,
        left = WindowInsets.systemBars.getLeft(density, LocalLayoutDirection.current),
        right = WindowInsets.systemBars.getLeft(density, LocalLayoutDirection.current),
        bottom = WindowInsets.systemBars.getBottom(density) / 3
    )
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        windowInsets = customWindowInsets
    ) {
        NAV_BAR_ROUTES.forEach { topLevelRoute ->
            val isSelected = backStack.lastOrNull() == topLevelRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigateToTopLevelDestination(topLevelRoute)
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) topLevelRoute.selectedIcon
                            ?: Icons.Default.QuestionMark else topLevelRoute.icon
                            ?: Icons.Default.QuestionMark,
                        contentDescription = topLevelRoute.name,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .scale(1.25f)
                    )
                }
            )
        }
    }
}

@Composable
fun ModalNavigationDrawerContent(
    backStack: SnapshotStateList<Any>,
    navigateToTopLevelDestination: (Route) -> Unit,
    onDrawerClicked: () -> Unit = {},
) {
    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface) {
        Column(modifier = Modifier.fillMaxHeight()) {
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
                    IconButton(onClick = onDrawerClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                            contentDescription = "Close drawer"
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                NAV_RAIL_ROUTES.forEach { topLevelRoute ->
                    NavigationDrawerItem(
                        selected = backStack.lastOrNull() == topLevelRoute,
                        label = {
                            Text(
                                text = topLevelRoute.name,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = topLevelRoute.selectedIcon
                                    ?: Icons.Default.QuestionMark,
                                contentDescription = topLevelRoute.name
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        ),
                        onClick = {
                            onDrawerClicked()
                            navigateToTopLevelDestination(topLevelRoute)
                        }
                    )
                }
            }
        }
    }
}
