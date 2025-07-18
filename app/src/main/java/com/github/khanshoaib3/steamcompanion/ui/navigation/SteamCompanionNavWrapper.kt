package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue.Expanded
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.window.core.layout.WindowSizeClass
import com.github.khanshoaib3.steamcompanion.ui.navigation.components.SteamCompanionNavBar
import com.github.khanshoaib3.steamcompanion.ui.navigation.components.SteamCompanionNavRail
import com.github.khanshoaib3.steamcompanion.ui.utils.NAV_OTHER_ROUTES
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.removeTopPadding
import kotlinx.coroutines.launch

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

    if (backStack.lastOrNull() in NAV_OTHER_ROUTES) {
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
    val view = LocalView.current

    BackHandler(enabled = railState.targetValue == Expanded) {
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
                    navigateTo = navigateTo
                )
            }
            SteamCompanionNavRail(
                backStack = backStack,
                railState = railState,
                navigateTo = {
                    coroutineScope.launch {
                        navigateTo(it)
                        if (railState.targetValue == Expanded) {
                            railState.collapse()
                        }
                    }
                },
                onRailButtonClicked = {
                    coroutineScope.launch {
                        railState.toggle()
                        if (railState.targetValue == Expanded) {
                            view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                        }
                    }
                },
                hideOnCollapse = navLayoutType == NavigationSuiteType.NavigationBar
            )
        }
    ) {
        SteamCompanionNavSuiteScope(navLayoutType, railState).content()
    }
}
