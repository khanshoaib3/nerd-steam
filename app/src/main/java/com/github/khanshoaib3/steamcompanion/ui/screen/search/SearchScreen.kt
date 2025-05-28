package com.github.khanshoaib3.steamcompanion.ui.screen.search

import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import coil3.compose.AsyncImage
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.search.components.SearchResultRow
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.removeBottomPadding
import kotlinx.coroutines.Dispatchers
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

    var searchResults: List<AppSearchResultDisplay> by remember { mutableStateOf(listOf()) }
    val focusManager = LocalFocusManager.current

    val onSearch: (String) -> Unit = {
        focusManager.clearFocus()
        scope.launch(Dispatchers.IO) {
            searchResults = searchViewModel.runSearchQuery(it)
            localView.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
    }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    SearchScreenWithScaffold(
        onSearch = onSearch,
        searchResults = searchResults,
        onGameClick = onGameClick,
        showMenuButton = true,
        onMenuButtonClick = onMenuButtonClick,
        scrollBehavior = scrollBehavior,
        currentDestination = currentDestination,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        modifier = modifier
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
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    onGameClick: (Int) -> Unit,
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    currentDestination: NavDestination?,
    imageWidth: Dp,
    imageHeight: Dp,
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
            onSearch = onSearch,
            searchResults = searchResults,
            onGameClick = onGameClick,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = modifier.padding(innerPadding.removeBottomPadding())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    searchResults: List<AppSearchResultDisplay>,
    onGameClick: (Int) -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState()
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarDefaults.InputField(
            query = textFieldState.text.toString(),
            onQueryChange = { textFieldState.edit { replace(0, length, it) } },
            onSearch = { onSearch(it) },
            placeholder = { Text("Search..") },
            expanded = false,
            onExpandedChange = {},
            leadingIcon = {
                IconButton(onClick = {textFieldState.edit { replace(0, length, "") }}) {
                    Icon(
                        if (textFieldState.text.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
                        contentDescription = if (textFieldState.text.isEmpty()) "Enter search query" else "Clear input text"
                    )
                }
            },
            trailingIcon = {
                if (!textFieldState.text.isEmpty())
                    IconButton(onClick = { onSearch(textFieldState.text.toString()) }) {
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
            onGameClick = {},
            showMenuButton = true,
            onMenuButtonClick = {},
            currentDestination = null,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
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
            searchResults = listOf(),
            onGameClick = {},
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}
