package com.github.khanshoaib3.steamcompanion.ui.screen.search

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.search.components.SearchResultRow
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.removeBottomPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(
    backStack: SnapshotStateList<Any>,
    navSuiteType: NavigationSuiteType,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchDataState by searchViewModel.searchDataState.collectAsState()

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

    val focusManager = LocalFocusManager.current

    val onSearch: (String) -> Unit = {
        focusManager.clearFocus()
        scope.launch(Dispatchers.IO) {
            searchViewModel.runSearchQuery(it)
            localView.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
    }

    val onSearchQueryChange: (String) -> Unit = {
        searchViewModel.updateSearchQuery(it)
    }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }


    if (navSuiteType == NavigationSuiteType.NavigationBar) {
        SearchListDetailScaffold(
            onSearch = onSearch,
            searchResults = searchDataState.searchResults,
            searchQuery = searchDataState.searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            navSuiteType = navSuiteType,
            navigator = navigator,
            paneExpansionState = null,
            onGameClick = onGameClick,
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
            SearchListDetailScaffold(
                onSearch = onSearch,
                searchResults = searchDataState.searchResults,
                searchQuery = searchDataState.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                navSuiteType = navSuiteType,
                navigator = navigator,
                paneExpansionState = paneExpansionState,
                onGameClick = onGameClick,
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
fun SearchListDetailScaffold(
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    navSuiteType: NavigationSuiteType,
    navigator: ThreePaneScaffoldNavigator<Any>,
    paneExpansionState: PaneExpansionState?,
    onGameClick: (Int) -> Unit,
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
                    SearchScreenWithScaffold(
                        onSearch = onSearch,
                        searchResults = searchResults,
                        searchQuery = searchQuery,
                        onSearchQueryChange = onSearchQueryChange,
                        onGameClick = onGameClick,
                        showMenuButton = true,
                        onMenuButtonClick = onMenuButtonClick,
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                        backStack = backStack,
                        imageWidth = imageWidth,
                        imageHeight = imageHeight,
                    )
                } else {
                    SearchScreen(
                        onSearch = onSearch,
                        searchResults = searchResults,
                        searchQuery = searchQuery,
                        onSearchQueryChange = onSearchQueryChange,
                        onGameClick = onGameClick,
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
fun SearchScreenWithScaffold(
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onGameClick: (Int) -> Unit,
    showMenuButton: Boolean,
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
                showMenuButton = showMenuButton,
                onMenuButtonClick = onMenuButtonClick,
                scrollBehavior = topAppBarScrollBehavior,
                backStack = backStack
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        SearchScreen(
            onSearch = onSearch,
            searchResults = searchResults,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onGameClick = onGameClick,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = modifier.padding(innerPadding.removeBottomPadding())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onGameClick: (Int) -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarDefaults.InputField(
            query = searchQuery,
            onQueryChange = { onSearchQueryChange(it) },
            onSearch = { onSearch(it) },
            placeholder = { Text("Search..") },
            expanded = false,
            onExpandedChange = {},
            leadingIcon = {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        if (searchQuery.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
                        contentDescription = if (searchQuery.isEmpty()) "Enter search query" else "Clear input text"
                    )
                }
            },
            trailingIcon = {
                if (!searchQuery.isEmpty())
                    IconButton(onClick = { onSearch(searchQuery) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send search query"
                        )
                    }
            }
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(searchResults) {
                SearchResultRow(
                    it,
                    onClick = onGameClick,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight
                )
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenWithScaffoldPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    SteamCompanionTheme {
        SearchScreenWithScaffold(
            onSearch = {},
            searchResults = listOf(),
            searchQuery = "Ello",
            onSearchQueryChange = {},
            onGameClick = {},
            showMenuButton = true,
            onMenuButtonClick = {},
            backStack = mutableStateListOf(Route.Search),
            topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    SteamCompanionTheme {
        SearchScreen(
            onSearch = {},
            searchResults = listOf(
                AppSearchResultDisplay(
                    appId = 12342,
                    name = "Max Payne 2: The Fall of Max Payne",
                    iconUrl = "bru"
                ),
                AppSearchResultDisplay(
                    appId = 12342,
                    name = "Max Payne 2: The Fall of Max Payne",
                    iconUrl = "bru"
                ),
            ),
            searchQuery = "Ello",
            onSearchQueryChange = {},
            onGameClick = {},
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}
