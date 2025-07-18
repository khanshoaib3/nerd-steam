package com.github.khanshoaib3.steamcompanion.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.home.HomeScreenRoot
import com.github.khanshoaib3.steamcompanion.ui.screen.search.SearchScreenRoot

@Composable
fun SteamCompanionNavDisplay(
    topLevelBackStack: TopLevelBackStack<Any>,
    navSuiteType: NavigationSuiteType,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = topLevelBackStack.backStack,
        modifier = modifier,
        onBack = { topLevelBackStack.removeLast() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Home> {
                HomeScreenRoot(
                    onMenuButtonClick = onMenuButtonClick,
                    navSuiteType = navSuiteType,
                    backStack = topLevelBackStack.backStack,
                )
            }
            entry<Route.Search> {
                SearchScreenRoot(
                    backStack = topLevelBackStack.backStack,
                    navSuiteType = navSuiteType,
                    onMenuButtonClick = onMenuButtonClick
                )
            }

            entry<Route.AppDetail> {
                AppDetailsScreen(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    appId = it.appId,
                    showTopBar = true,
                    onUpButtonClick = { /* TODO (proposal) Navigate to home screen */ }
                )
            }

            entry<Route.Bookmark> {
                BookmarkScreenRoot(
                    onMenuButtonClick = onMenuButtonClick,
                    backStack = topLevelBackStack.backStack,
                    navSuiteType = navSuiteType
                )
            }
        }
    )
}
