package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.TwoPaneScene
import com.github.khanshoaib3.steamcompanion.ui.components.TwoPaneSceneStrategy
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.GameDetailViewModel
import com.github.khanshoaib3.steamcompanion.ui.screen.home.HomeScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.search.SearchScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack

@Composable
fun SteamCompanionNavDisplay(
    topLevelBackStack: TopLevelBackStack<Route>,
    navSuiteType: NavigationSuiteType,
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
    NavDisplay(
        backStack = topLevelBackStack.backStack,
        modifier = modifier,
        onBack = { topLevelBackStack.removeLast() },
        sceneStrategy = twoPaneStrategy,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Home>(
                metadata = TwoPaneScene.twoPane()
            ) {
                HomeScreenRoot(
                    onMenuButtonClick = onMenuButtonClick,
                    addAppDetailPane = addAppDetailPane,
                    navSuiteType = navSuiteType,
                    topLevelBackStack = topLevelBackStack
                )
            }
            entry<Route.Search>(
                metadata = TwoPaneScene.twoPane()
            ) {
                SearchScreenRoot(
                    topLevelBackStack = topLevelBackStack,
                    navSuiteType = navSuiteType,
                    onMenuButtonClick = onMenuButtonClick,
                    addAppDetailPane = addAppDetailPane,
                )
            }

            entry<Route.Bookmark> {
                BookmarkScreenRoot(
                    topLevelBackStack = topLevelBackStack,
                    onMenuButtonClick = { topLevelBackStack.removeLast() },
                    addAppDetailPane = addAppDetailPane
                )
            }

            entry<Route.AppDetail>(
                metadata = TwoPaneScene.twoPane()
            ) { key ->
                val viewModel = hiltViewModel<GameDetailViewModel, GameDetailViewModel.Factory>(
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
                    navSuiteType = navSuiteType,
                    onUpButtonClick = { topLevelBackStack.removeLast() },
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    viewModel = viewModel
                )
            }
        }
    )
}
