package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.github.khanshoaib3.steamcompanion.ui.utils.removeTopPadding
import kotlinx.coroutines.launch
import kotlin.Any

class SteamCompanionNavSuiteScope(
    val navSuiteType: NavigationSuiteType,
    val railState: WideNavigationRailState,
    val modifier: Modifier = Modifier
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

    val railState = rememberWideNavigationRailState()

    if (backStack.lastOrNull() == Route.Bookmark) {
        Scaffold {
            SteamCompanionNavSuiteScope(
                navSuiteType = navLayoutType,
                railState = railState,
                modifier = Modifier.padding(it.removeTopPadding())
            ).content()
        }
        return
    }

    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = railState.targetValue == WideNavigationRailValue.Expanded) {
        coroutineScope.launch {
            railState.collapse()
        }
    }


    NavigationSuiteScaffoldLayout(
        layoutType = navLayoutType,
        navigationSuite = {
            if (navLayoutType == NavigationSuiteType.NavigationBar) {
                SteamCompanionNavBar(
                    backStack = backStack,
                    navigateToTopLevelDestination = navigateTo
                )
            }
            SteamCompanionNavRail(
                backStack = backStack,
                railState = railState,
                navigateTo = navigateTo,
                onDrawerClicked = {
                    coroutineScope.launch {
                        railState.toggle()
                    }
                },
                hideOnCollapse = navLayoutType == NavigationSuiteType.NavigationBar
            )
        }
    ) {
        SteamCompanionNavSuiteScope(navLayoutType, railState).content()
    }
}

@Composable
fun SteamCompanionNavRail(
    backStack: SnapshotStateList<Any>,
    navigateTo: (Route) -> Unit,
    railState: WideNavigationRailState,
    onDrawerClicked: () -> Unit,
    hideOnCollapse: Boolean = false,
) {
    ModalWideNavigationRail(
        state = railState,
        hideOnCollapse = hideOnCollapse,
        header = {
            IconButton(
                modifier =
                    Modifier
                        .padding(start = 24.dp)
                        .semantics {
                            // The button must announce the expanded or collapsed state of the rail
                            // for accessibility.
                            stateDescription =
                                if (railState.currentValue == WideNavigationRailValue.Expanded) {
                                    "Expanded"
                                } else {
                                    "Collapsed"
                                }
                        },
                onClick = onDrawerClicked,
            ) {
                if (railState.targetValue == WideNavigationRailValue.Expanded) {
                    Icon(Icons.AutoMirrored.Filled.MenuOpen, "Collapse rail")
                } else {
                    Icon(Icons.Filled.Menu, "Expand rail")
                }
            }
        },
    ) {
        NAV_RAIL_ROUTES.forEach { route ->
            val isSelected = backStack.lastOrNull() == route
            WideNavigationRailItem(
                railExpanded = railState.targetValue == WideNavigationRailValue.Expanded,
                icon = {
                    val imageVector = (if (isSelected) route.selectedIcon else route.icon)
                        ?: Icons.Default.QuestionMark
                    Icon(imageVector = imageVector, contentDescription = route.name)
                },
                label = { Text(route.name) },
                selected = isSelected,
                onClick = { navigateTo(route) },
            )
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
        NAV_BAR_ROUTES.forEach { route ->
            val isSelected = backStack.lastOrNull() == route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigateToTopLevelDestination(route)
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                },
                icon = {
                    Icon(
                        imageVector = (if (isSelected) route.selectedIcon else route.icon)
                            ?: Icons.Default.QuestionMark,
                        contentDescription = route.name,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .scale(1.25f)
                    )
                }
            )
        }
    }
}
