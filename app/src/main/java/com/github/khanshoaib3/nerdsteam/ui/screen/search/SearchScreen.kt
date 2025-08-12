package com.github.khanshoaib3.nerdsteam.ui.screen.search

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.ui.navigation.components.CommonTopAppBar
import com.github.khanshoaib3.nerdsteam.ui.screen.search.components.SearchResultRow
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme
import com.github.khanshoaib3.nerdsteam.ui.utils.Route
import com.github.khanshoaib3.nerdsteam.ui.utils.TopLevelRoute
import com.github.khanshoaib3.nerdsteam.utils.Progress
import com.github.khanshoaib3.nerdsteam.utils.TopLevelBackStack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(
    topLevelBackStack: TopLevelBackStack<Route>,
    isWideScreen: Boolean,
    isShowingNavRail: Boolean,
    onMenuButtonClick: () -> Unit,
    addAppDetailPane: (Int) -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner),
) {
    val dataState by searchViewModel.searchDataState.collectAsState()
    val viewState by searchViewModel.searchViewState.collectAsState()

    val localView = LocalView.current

    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    val scope = rememberCoroutineScope()

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

    if (isWideScreen) {
        SearchScreen(
            onSearch = onSearch,
            searchResults = dataState.searchResults,
            searchStatus = viewState.searchStatus,
            searchQuery = dataState.searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onGameClick = addAppDetailPane,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = modifier
        )
    } else {
        SearchScreenWithScaffold(
            onSearch = onSearch,
            searchResults = dataState.searchResults,
            searchStatus = viewState.searchStatus,
            searchQuery = dataState.searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onGameClick = addAppDetailPane,
            showMenuButton = !isShowingNavRail,
            onMenuButtonClick = onMenuButtonClick,
            navigateBackCallback = { topLevelBackStack.removeLast() },
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenWithScaffold(
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    searchStatus: Progress,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onGameClick: (Int) -> Unit,
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    navigateBackCallback: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                showMenuButton = showMenuButton,
                onMenuButtonClick = onMenuButtonClick,
                scrollBehavior = topAppBarScrollBehavior,
                navigateBackCallback = navigateBackCallback,
                forRoute = TopLevelRoute.Search,
                windowInsets = WindowInsets()
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        SearchScreen(
            onSearch = onSearch,
            searchResults = searchResults,
            searchStatus = searchStatus,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onGameClick = onGameClick,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = modifier.padding(
                top = if (searchStatus == Progress.NOT_QUEUED) Dp.Unspecified else innerPadding.calculateTopPadding()
            )
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    searchStatus: Progress,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onGameClick: (Int) -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    LookaheadScope {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = if (searchStatus != Progress.LOADED) Modifier.animateBounds(this@LookaheadScope) else Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (searchStatus is Progress.NOT_QUEUED) {
                    Text(
                        text = "Enter Something",
                        style = MaterialTheme.typography.headlineLargeEmphasized.copy(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                )
                            )
                        )
                    )
                }
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = { onSearchQueryChange(it) },
                    onSearch = { onSearch(it) },
                    expanded = false,
                    placeholder = { Text("Search") },
                    onExpandedChange = {},
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_large)),
                    leadingIcon = {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                if (searchQuery.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
                                contentDescription = if (searchQuery.isEmpty()) "Enter search query" else "Clear input text"
                            )
                        }
                    },
                    trailingIcon = {
                        if (!searchQuery.isEmpty()) {
                            IconButton(onClick = { onSearch(searchQuery) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send search query"
                                )
                            }
                        }
                    }
                )
            }

            when (searchStatus) {
                Progress.NOT_QUEUED -> {}

                is Progress.FAILED -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(searchStatus.reason ?: "Unable to search")
                    }
                }

                Progress.LOADING,
                    -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        LoadingIndicator()
                    }
                }

                Progress.LOADED -> {
                    OutlinedCard(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                            .weight(1f),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
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
                    }
                }
            }
        }
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

    NerdSteamTheme {
        SearchScreenWithScaffold(
            onSearch = {},
            searchResults = listOf(),
            searchStatus = Progress.NOT_QUEUED,
            searchQuery = "",
            onSearchQueryChange = {},
            onGameClick = {},
            showMenuButton = true,
            onMenuButtonClick = {},
            navigateBackCallback = {},
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

    NerdSteamTheme {
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
            searchStatus = Progress.LOADED,
            searchQuery = "Ello",
            onSearchQueryChange = {},
            onGameClick = {},
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}