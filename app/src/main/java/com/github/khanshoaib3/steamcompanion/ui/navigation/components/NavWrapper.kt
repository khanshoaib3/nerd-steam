package com.github.khanshoaib3.steamcompanion.ui.navigation.components

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue.Expanded
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import kotlinx.coroutines.launch

class NavSuiteScope @OptIn(ExperimentalMaterial3Api::class) constructor(
    val railState: WideNavigationRailState,
    val topAppBarScrollBehavior: TopAppBarScrollBehavior,
    val modifier: Modifier = Modifier,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavWrapper(
    currentTopLevelRoute: Route,
    navigateTo: (Route) -> Unit,
    showNavRail: Boolean,
    isWideScreen: Boolean,
    content: @Composable (NavSuiteScope.() -> Unit),
) {
    val railState = rememberWideNavigationRailState()

    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current

    BackHandler(enabled = railState.targetValue == Expanded) {
        coroutineScope.launch {
            railState.collapse()
        }
    }

    val density = LocalDensity.current
    var leftInsetInDp = 0.dp
    with(density) {
        leftInsetInDp =
            WindowInsets.systemBars.getLeft(density, LocalLayoutDirection.current).toDp()
    }
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .padding(start = if (showNavRail) leftInsetInDp + 96.dp else 0.dp),
        topBar = {
            if (isWideScreen) {
                CommonTopAppBar(
                    showMenuButton = false,
                    onMenuButtonClick = {},
                    navigateBackCallback = {},
                    scrollBehavior = topAppBarScrollBehavior,
                    forRoute = currentTopLevelRoute,
                )
            }
        },
        bottomBar = {
            if (!showNavRail) {
                NavBar(
                    currentTopLevelRoute = currentTopLevelRoute,
                    navigateTo = navigateTo
                )
            }
        },
    ) { innerPaddings ->
        NavSuiteScope(
            railState = railState,
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            modifier = Modifier.padding(innerPaddings),
        ).content()
    }

    NavRail(
        currentTopLevelRoute = currentTopLevelRoute,
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
        showNavRail = showNavRail
    )
}
