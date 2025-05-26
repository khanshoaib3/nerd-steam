package com.github.khanshoaib3.steamcompanion.ui.screen.search

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkDisplay
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkScreenWithScaffold
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(
    currentDestination: NavDestination?,
    navSuiteType: NavigationSuiteType,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val localView = LocalView.current

    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val scope = rememberCoroutineScope()

    val onGameClick: (Int) -> Unit = {
        scope.launch {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Detail,
                contentKey = it
            )
        }
    }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    SearchScreenWithScaffold(
        showMenuButton = true,
        onMenuButtonClick = onMenuButtonClick,
        scrollBehavior = scrollBehavior,
        currentDestination = currentDestination
    )

    /*
    if (navSuiteType == NavigationSuiteType.NavigationBar) {
        BookmarkListDetailScaffold(
            navSuiteType = navSuiteType,
            navigator = navigator,
            paneExpansionState = null,
            bookmarks = sortedBookmarks,
            onGameClick = onGameClick,
            onGameHeaderClick = onGameHeaderClick,
            onTimeHeaderClick = onTimeHeaderClick,
            onMenuButtonClick = onMenuButtonClick,
            topAppBarScrollBehavior = scrollBehavior,
            currentDestination = currentDestination,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            onListPaneUpButtonClick = {
                scope.launch {
                    navigator.navigateBack()
                }
            },
        )
    } else {
        // https://stackoverflow.com/a/79314221/12026423
        val paneExpansionState = rememberPaneExpansionState()
        paneExpansionState.setFirstPaneProportion(0.45f)

        Scaffold(topBar = {
            SteamCompanionTopAppBar(
                scrollBehavior = scrollBehavior,
                showMenuButton = false,
                onMenuButtonClick = onMenuButtonClick,
                currentDestination = currentDestination
            )
        }) { innerPadding ->
            BookmarkListDetailScaffold(
                navSuiteType = navSuiteType,
                navigator = navigator,
                paneExpansionState = paneExpansionState,
                bookmarks = sortedBookmarks,
                onGameClick = onGameClick,
                onGameHeaderClick = onGameHeaderClick,
                onTimeHeaderClick = onTimeHeaderClick,
                onMenuButtonClick = onMenuButtonClick,
                topAppBarScrollBehavior = scrollBehavior,
                currentDestination = currentDestination,
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                onListPaneUpButtonClick = {},
                modifier = modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                )
            )
        }
    }
    */
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenWithScaffold(
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                showMenuButton = showMenuButton,
                onMenuButtonClick = onMenuButtonClick,
                scrollBehavior = scrollBehavior,
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        SearchScreen(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("This is the search screen!!")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenWithScaffoldPreview() {
    SteamCompanionTheme {
        SearchScreenWithScaffold(
            showMenuButton = true,
            onMenuButtonClick = {},
            currentDestination = null,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenPreview() {
    SteamCompanionTheme {
        SearchScreen()
    }
}
