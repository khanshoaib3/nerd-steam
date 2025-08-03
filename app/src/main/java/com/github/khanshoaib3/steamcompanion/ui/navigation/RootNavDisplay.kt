package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.github.khanshoaib3.steamcompanion.ui.components.TwoPaneScene
import com.github.khanshoaib3.steamcompanion.ui.navigation.components.NavWrapper
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.AppDetailViewModel
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.TopLevelRoute
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack
import kotlinx.coroutines.launch

@Composable
fun RootNavDisplay(
    rootBackStack: NavBackStack,
    topLevelBackStack: TopLevelBackStack<Route>,
) {
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = adaptiveInfo.windowSizeClass

    val showNavRail = when {
        adaptiveInfo.windowPosture.isTabletop -> false
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
            -> true

        else -> false
    }
    val isWideScreen = when {
        !showNavRail -> false
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
                && !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_EXPANDED_LOWER_BOUND)
            -> true
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
                && !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND)
            -> true

        else -> false
    }

    Scaffold { innerPadding ->
        NavDisplay(
            backStack = rootBackStack,
            onBack = { rootBackStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<Route.Bookmark>(
                    metadata = TwoPaneScene.setAsFirst()
                ) {
                    BookmarkScreenRoot(
                        navigateBackCallback = { rootBackStack.removeLastOrNull() },
                        addAppDetailPane = { rootBackStack.add(Route.AppDetail(it)) },
                        modifier = Modifier.padding(innerPadding),
                    )
                }

                entry<Route.AppDetail>(
                    metadata = TwoPaneScene.setAsSecond()
                ) { key ->
                    val viewModel =
                        hiltViewModel<AppDetailViewModel, AppDetailViewModel.Factory>(
                            // Note: We need a new ViewModel for every new RouteB instance. Usually
                            // we would need to supply a `key` String that is unique to the
                            // instance, however, the ViewModelStoreNavEntryDecorator (supplied
                            // above) does this for us, using `NavEntry.contentKey` to uniquely
                            // identify the viewModel.
                            //
                            // tl;dr: Make sure you use rememberViewModelStoreNavEntryDecorator()
                            // if you want a new ViewModel for each new navigation key instance.
                            creationCallback = { factory ->
                                factory.create(key)
                            }
                        )
                    AppDetailsScreen(
                        isWideScreen = isWideScreen,
                        isInTwoPaneScene = false,
                        onUpButtonClick = { rootBackStack.removeLastOrNull() },
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }

                entry<TopLevelRoute.Dummy> {
                    NavWrapper(
                        currentTopLevelRoute = topLevelBackStack.topLevelKey,
                        showNavRail = showNavRail,
                        isWideScreen = isWideScreen,
                        navigateTo = {
                            if (it is TopLevelRoute) topLevelBackStack.addTopLevel(it)
                            else rootBackStack.add(it)
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        },
                    ) {
                        TopLevelNavDisplay(
                            topLevelBackStack = topLevelBackStack,
                            isWideScreen = isWideScreen,
                            isShowingNavRail = showNavRail,
                            onMenuButtonClick = {
                                scope.launch {
                                    railState.expand()
                                    view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                                }
                            },
                            modifier = modifier
                        )
                    }
                }
            },
            transitionSpec = {
                if (rootBackStack.lastOrNull() is Route.AppDetail) {
                    // Slide in from right when navigating forward
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { it } togetherWith
                            slideOutHorizontally(tween(easing = LinearOutSlowInEasing)) { -it }
                } else {
                    scaleIn(spring(stiffness = Spring.StiffnessLow)) + fadeIn() togetherWith fadeOut()
                }
            },

            popTransitionSpec = {
                if (rootBackStack.lastOrNull() is Route.AppDetail) {
                    // Slide in from left when navigating back
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) togetherWith
                            slideOutHorizontally(animationSpec = tween(easing = LinearOutSlowInEasing)) { it }
                } else {
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { (-it * 0.2).toInt() } + fadeIn() togetherWith
                            scaleOut(animationSpec = tween(easing = LinearOutSlowInEasing)) + fadeOut()
                }
            },
            predictivePopTransitionSpec = {
                if (rootBackStack.lastOrNull() is Route.AppDetail) {
                    // Slide in from left when navigating back
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) togetherWith
                            slideOutHorizontally(animationSpec = tween(easing = LinearOutSlowInEasing)) { it }
                } else {
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { (-it * 0.2).toInt() } + fadeIn() togetherWith
                            scaleOut(animationSpec = tween(easing = LinearOutSlowInEasing)) + fadeOut()
                }
            }
        )
    }
}
