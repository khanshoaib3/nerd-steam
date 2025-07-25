package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.github.khanshoaib3.steamcompanion.ui.components.TwoPaneScene
import com.github.khanshoaib3.steamcompanion.ui.navigation.components.NavWrapper
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.GameDetailViewModel
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
                        backStack = rootBackStack,
                        onMenuButtonClick = { rootBackStack.removeLastOrNull() },
                        addAppDetailPane = { rootBackStack.add(Route.AppDetail(it)) },
                        modifier = Modifier.padding(innerPadding),
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
                        isWideScreen = false,
                        onUpButtonClick = { rootBackStack.removeLastOrNull() },
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }

                entry<TopLevelRoute.Dummy> {
                    NavWrapper(
                        currentRoute = topLevelBackStack.getLast()
                            ?: error("Current route is null!!"),
                        currentTopLevelRoute = topLevelBackStack.topLevelKey,
                        navigateTo = {
                            if (it is TopLevelRoute) topLevelBackStack.addTopLevel(it)
                            else rootBackStack.add(it)
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        },
                    ) {
                        TopLevelNavDisplay(
                            topLevelBackStack = topLevelBackStack,
                            isWideScreen = isWideScreen,
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
            }
        )
    }
}