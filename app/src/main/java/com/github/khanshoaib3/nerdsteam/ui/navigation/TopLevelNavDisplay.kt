package com.github.khanshoaib3.nerdsteam.ui.navigation

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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
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
import com.github.khanshoaib3.nerdsteam.ui.components.TwoPaneScene
import com.github.khanshoaib3.nerdsteam.ui.components.TwoPaneSceneStrategy
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppDetailsScreenRoot
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppDetailViewModel
import com.github.khanshoaib3.nerdsteam.ui.screen.home.HomeScreenRoot
import com.github.khanshoaib3.nerdsteam.ui.screen.search.SearchScreenRoot
import com.github.khanshoaib3.nerdsteam.ui.utils.Route
import com.github.khanshoaib3.nerdsteam.ui.utils.TopLevelRoute
import com.github.khanshoaib3.nerdsteam.utils.TopLevelBackStack

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopLevelNavDisplay(
    topLevelBackStack: TopLevelBackStack<Route>,
    isWideScreen: Boolean,
    isShowingNavRail: Boolean,
    onMenuButtonClick: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    val twoPaneStrategy = remember { TwoPaneSceneStrategy<Any>() }
    val addAppDetailPane: (Int) -> Unit = {
        if (topLevelBackStack.getLast() is Route.AppDetail) topLevelBackStack.removeLast()
        topLevelBackStack.add(Route.AppDetail(it))
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    SharedTransitionLayout {
        // Ref: https://github.com/android/nav3-recipes/blob/6982b0fd0ff6a5e409df9ec7d7c00bcbc1c04785/app/src/main/java/com/example/nav3recipes/scenes/twopane/TwoPaneActivity.kt#L89
        val sharedEntryInSceneNavEntryDecorator = navEntryDecorator<NavKey> { entry ->
            with(this) {
                Box(
                    Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(entry.contentKey),
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
                        topLevelBackStack = topLevelBackStack,
                        isWideScreen = isWideScreen,
                        isShowingNavRail = isShowingNavRail,
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                    )
                }
                entry<TopLevelRoute.Search>(
                    metadata = TwoPaneScene.setAsFirst()
                ) {
                    SearchScreenRoot(
                        topLevelBackStack = topLevelBackStack,
                        isWideScreen = isWideScreen,
                        isShowingNavRail = isShowingNavRail,
                        onMenuButtonClick = onMenuButtonClick,
                        addAppDetailPane = addAppDetailPane,
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
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
                    AppDetailsScreenRoot(
                        viewModel = viewModel,
                        isWideScreen = isWideScreen,
                        isInTwoPaneScene = true,
                        onUpButtonClick = { topLevelBackStack.removeLast() },
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                    )
                }
            },
            transitionSpec = {
                if (TwoPaneScene.IsActive && topLevelBackStack.lastTopLevelKey != topLevelBackStack.topLevelKey) {
                    // When changing between app details in two pane screen view (performs the default fade transitions)
                    ContentTransform(
                        fadeIn(animationSpec = tween(700)),
                        fadeOut(animationSpec = tween(700)),
                    )
                } else if (topLevelBackStack.lastTopLevelKey != topLevelBackStack.topLevelKey) {
                    // When changing between top level screens only do minimal transitions
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { (it * 0.2).toInt() } togetherWith fadeOut()
                } else {
                    // Slide in from right when navigating forward
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { it } togetherWith
                            slideOutHorizontally(tween(easing = LinearOutSlowInEasing)) { -it }
                }
            },
            popTransitionSpec = {
                if (topLevelBackStack.lastTopLevelKey != topLevelBackStack.topLevelKey) {
                    // When changing between top level screens only do minimal transitions
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { (-it * 0.2).toInt() } togetherWith fadeOut()
                } else {
                    // Slide in from left when navigating back
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) togetherWith
                            slideOutHorizontally(animationSpec = tween(easing = LinearOutSlowInEasing)) { it }
                }
            },
            predictivePopTransitionSpec = {
                if (topLevelBackStack.lastTopLevelKey != topLevelBackStack.topLevelKey) {
                    // When changing between top level screens only do minimal transitions
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) { (-it * 0.2).toInt() } + fadeIn() togetherWith fadeOut()
                } else {
                    // Slide in from left when navigating back
                    slideInHorizontally(spring(stiffness = Spring.StiffnessLow)) togetherWith
                            slideOutHorizontally(animationSpec = tween(easing = LinearOutSlowInEasing)) { it }
                }
            }
        )
    }
}