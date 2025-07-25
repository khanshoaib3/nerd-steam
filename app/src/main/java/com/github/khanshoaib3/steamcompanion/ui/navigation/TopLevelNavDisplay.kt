package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.navEntryDecorator
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.github.khanshoaib3.steamcompanion.ui.components.TwoPaneScene
import com.github.khanshoaib3.steamcompanion.ui.components.TwoPaneSceneStrategy
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.GameDetailViewModel
import com.github.khanshoaib3.steamcompanion.ui.screen.home.HomeScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.search.SearchScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.TopLevelRoute
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TopLevelNavDisplay(
    topLevelBackStack: TopLevelBackStack<Route>,
    isWideScreen: Boolean,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val twoPaneStrategy = remember { TwoPaneSceneStrategy<Any>() }
    val addAppDetailPane: (Int) -> Unit = {
        if (topLevelBackStack.getLast() is Route.AppDetail) topLevelBackStack.removeLast()
        topLevelBackStack.add(Route.AppDetail(it))
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    SharedTransitionLayout {
        /**
         * A [NavEntryDecorator] that wraps each entry in a shared element that is controlled by the
         * [Scene].
         */
        val sharedEntryInSceneNavEntryDecorator = navEntryDecorator<NavKey> { entry ->
            with(this) {
                Box(
                    Modifier.sharedBounds(
                        rememberSharedContentState(entry.contentKey),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(contentScale = ContentScale.FillBounds)
                    ),
                ) {
                    entry.Content()
                }
            }
        }

        NavDisplay(
            backStack = topLevelBackStack.backStack,
            modifier = modifier,
            onBack = { topLevelBackStack.removeLast() },
            sceneStrategy = twoPaneStrategy,
            entryDecorators = listOf(
                sharedEntryInSceneNavEntryDecorator,
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<TopLevelRoute.Home>(
                    metadata = TwoPaneScene.setAsFirst(),
                ) {
                    HomeScreenRoot(
                        onMenuButtonClick = onMenuButtonClick,
                        addAppDetailPane = addAppDetailPane,
                        isWideScreen = isWideScreen,
                        topLevelBackStack = topLevelBackStack,
                    )
                }
                entry<TopLevelRoute.Search>(
                    metadata = TwoPaneScene.setAsFirst()
                ) {
                    SearchScreenRoot(
                        topLevelBackStack = topLevelBackStack,
                        isWideScreen = isWideScreen,
                        onMenuButtonClick = onMenuButtonClick,
                        addAppDetailPane = addAppDetailPane,
                    )
                }

                entry<Route.AppDetail>(
                    metadata = TwoPaneScene.setAsSecond()
                ) { key ->
                    val viewModel =
                        hiltViewModel<GameDetailViewModel, GameDetailViewModel.Factory>(
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
                        onUpButtonClick = { topLevelBackStack.removeLast() },
                        viewModel = viewModel
                    )
                }
            },
            transitionSpec = {
                if (TwoPaneScene.InTwoPaneScene) {
                    ContentTransform(
                        fadeIn(animationSpec = tween(700)),
                        fadeOut(animationSpec = tween(700)),
                    )
                } else {
                    // Slide in from right when navigating forward
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(easing = LinearOutSlowInEasing)
                            )
                }
            },
            popTransitionSpec = {
                if (TwoPaneScene.InTwoPaneScene) {
                    ContentTransform(
                        fadeIn(animationSpec = tween(700)),
                        fadeOut(animationSpec = tween(700)),
                    )
                } else {
                    // Slide in from left when navigating back
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(easing = LinearOutSlowInEasing)
                            )
                }
            },
            predictivePopTransitionSpec = {
                if (TwoPaneScene.InTwoPaneScene) {
                    ContentTransform(
                        fadeIn(
                            spring(
                                dampingRatio = 1.0f, // reflects material3 motionScheme.defaultEffectsSpec()
                                stiffness = 1600.0f, // reflects material3 motionScheme.defaultEffectsSpec()
                            )
                        ),
                        scaleOut(targetScale = 0.7f),
                    )
                } else {
                    // Slide in from left when navigating back
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(easing = LinearOutSlowInEasing)
                            )
                }
            }
        )
    }
}