package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.components.BookmarkTable
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.removeBottomPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreenRoot(
    backStack: SnapshotStateList<Any>,
    navSuiteType: NavigationSuiteType,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val localView = LocalView.current
    val sortedBookmarks by bookmarkViewModel.sortedBookmarks.collectAsState()

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
    val onGameHeaderClick: () -> Unit = {
        bookmarkViewModel.toggleSortOrderOfTypeName()
        localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }
    val onTimeHeaderClick: () -> Unit = {
        bookmarkViewModel.toggleSortOrderOfTypeTime()
        localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

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
            backStack = backStack,
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
                backStack = backStack,
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
                backStack = backStack,
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                onListPaneUpButtonClick = {},
                modifier = modifier.padding(innerPadding.removeBottomPadding())
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookmarkListDetailScaffold(
    navSuiteType: NavigationSuiteType,
    navigator: ThreePaneScaffoldNavigator<Any>,
    paneExpansionState: PaneExpansionState?,
    bookmarks: List<BookmarkDisplay>,
    onGameClick: (Int) -> Unit,
    onGameHeaderClick: () -> Unit,
    onTimeHeaderClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    backStack: SnapshotStateList<Any>,
    imageWidth: Dp,
    imageHeight: Dp,
    onListPaneUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        paneExpansionState = paneExpansionState,
        listPane = {
            AnimatedPane {
                if (navSuiteType == NavigationSuiteType.NavigationBar) {
                    BookmarkScreenWithScaffold(
                        bookmarks = bookmarks,
                        onGameClick = onGameClick,
                        onGameHeaderClick = onGameHeaderClick,
                        onTimeHeaderClick = onTimeHeaderClick,
                        onMenuButtonClick = onMenuButtonClick,
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                        backStack = backStack,
                        imageWidth = imageWidth,
                        imageHeight = imageHeight
                    )
                } else {
                    BookmarkScreen(
                        bookmarks = bookmarks,
                        onGameClick = onGameClick,
                        onGameHeaderClick = onGameHeaderClick,
                        onTimeHeaderClick = onTimeHeaderClick,
                        imageWidth = imageWidth,
                        imageHeight = imageHeight,
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected item is available
                navigator.currentDestination?.contentKey.let {
                    AppDetailsScreen(
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                        appId = it as Int?,
                        showTopBar = navSuiteType == NavigationSuiteType.NavigationBar,
                        onUpButtonClick = onListPaneUpButtonClick
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreenWithScaffold(
    bookmarks: List<BookmarkDisplay>,
    onGameClick: (Int) -> Unit,
    onGameHeaderClick: () -> Unit,
    onTimeHeaderClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    backStack: SnapshotStateList<Any>,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                showMenuButton = true,
                onMenuButtonClick = onMenuButtonClick,
                backStack = backStack
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        BookmarkScreen(
            bookmarks,
            onGameClick,
            onGameHeaderClick,
            onTimeHeaderClick,
            imageWidth,
            imageHeight,
            modifier.padding(innerPadding.removeBottomPadding())
        )
    }
}

@Composable
fun BookmarkScreen(
    bookmarks: List<BookmarkDisplay>,
    onGameClick: (Int) -> Unit,
    onGameHeaderClick: () -> Unit,
    onTimeHeaderClick: () -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    BookmarkTable(
        bookmarks,
        onGameClick,
        onGameHeaderClick,
        onTimeHeaderClick,
        imageWidth,
        imageHeight,
        modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BookmarkScreenWithScaffoldPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    SteamCompanionTheme {
        BookmarkScreenWithScaffold(
            bookmarks = listOf(
                BookmarkDisplay(
                    appId = 1231,
                    name = "Max Payne: The Fall of Max Payne",
                    formattedTime = "dd MMM yyyy"
                )
            ),
            onGameClick = {},
            onGameHeaderClick = {},
            onTimeHeaderClick = {},
            onMenuButtonClick = {},
            topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            backStack = mutableStateListOf(Route.Bookmark),
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BookmarkScreenPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    SteamCompanionTheme {
        BookmarkScreen(
            bookmarks = listOf(
                BookmarkDisplay(
                    appId = 1231,
                    name = "Max Payne: The Fall of Max Payne",
                    formattedTime = "dd MMM yyyy"
                ),
            ),
            onGameClick = {},
            onGameHeaderClick = {},
            onTimeHeaderClick = {},
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}