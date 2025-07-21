package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.GameDetailViewModel
import com.github.khanshoaib3.steamcompanion.ui.screen.home.HomeScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.search.SearchScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SteamCompanionNavDisplay(
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

    val localNavSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
        compositionLocalOf {
            throw IllegalStateException(
                "Unexpected access to LocalNavSharedTransitionScope. You must provide a " +
                        "SharedTransitionScope from a call to SharedTransitionLayout() or " +
                        "SharedTransitionScope()"
            )
        }

    /**
     * A [NavEntryDecorator] that wraps each entry in a shared element that is controlled by the
     * [Scene].
     */
    val sharedEntryInSceneNavEntryDecorator = navEntryDecorator<NavKey> { entry ->
        with(localNavSharedTransitionScope.current) {
            Box(
                Modifier.sharedElement(
                    rememberSharedContentState(entry.contentKey),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                ),
            ) {
                entry.Content()
            }
        }
    }


    SharedTransitionLayout {
        CompositionLocalProvider(localNavSharedTransitionScope provides this) {
            NavDisplay(
                backStack = topLevelBackStack.backStack,
                modifier = modifier,
                onBack = { topLevelBackStack.removeLast() },
                sceneStrategy = twoPaneStrategy,
                entryDecorators = listOf(
                    rememberSceneSetupNavEntryDecorator(),
                    rememberSavedStateNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<Route.Home>(
                        metadata = TwoPaneScene.setAsFirst()
                    ) {
                        HomeScreenRoot(
                            onMenuButtonClick = onMenuButtonClick,
                            addAppDetailPane = addAppDetailPane,
                            isWideScreen = isWideScreen,
                            topLevelBackStack = topLevelBackStack
                        )
                    }
                    entry<Route.Search>(
                        metadata = TwoPaneScene.setAsFirst()
                    ) {
                        SearchScreenRoot(
                            topLevelBackStack = topLevelBackStack,
                            isWideScreen = isWideScreen,
                            onMenuButtonClick = onMenuButtonClick,
                            addAppDetailPane = addAppDetailPane,
                        )
                    }

                    entry<Route.Bookmark>(
                        metadata = TwoPaneScene.setAsFirst()
                    ) {
                        BookmarkScreenRoot(
                            topLevelBackStack = topLevelBackStack,
                            onMenuButtonClick = { topLevelBackStack.removeLast() },
                            addAppDetailPane = addAppDetailPane
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
                    // Slide in from right when navigating forward
                    slideInHorizontally( initialOffsetX = { it }, animationSpec = tween(1000)) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                },
                popTransitionSpec = {
                    // Slide in from left when navigating back
                    slideInHorizontally( initialOffsetX = { -it/2 }, animationSpec = tween(1000)) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
                predictivePopTransitionSpec = {
                    // Slide in from left when navigating back
                    slideInHorizontally( initialOffsetX = { -it/2 }, animationSpec = tween(1000)) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                }
            )
        }
    }
}
