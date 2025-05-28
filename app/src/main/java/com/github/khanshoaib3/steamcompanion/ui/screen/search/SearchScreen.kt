package com.github.khanshoaib3.steamcompanion.ui.screen.search

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.search.components.SearchInputField
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val textFieldState: TextFieldState = rememberTextFieldState()
    // Controls expansion state of the search bar
    var searchResults: List<String> by remember { mutableStateOf(listOf()) }

    val focusManager = LocalFocusManager.current
    val onSearch: () -> Unit = {
        searchResults = listOf(
            "II",
            "Star wars",
            "Mission Impossible",
            "Sukuna",
            "Ryomen"
        )
        focusManager.clearFocus()
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarDefaults.InputField(
            query = textFieldState.text.toString(),
            onQueryChange = { textFieldState.edit { replace(0, length, it) } },
            onSearch = { onSearch.invoke() },
            placeholder = { Text("Search..") },
            expanded = false,
            onExpandedChange = {},
            leadingIcon = {
                Icon(
                    if (textFieldState.text.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
                    contentDescription = if (textFieldState.text.isEmpty()) "Enter search query" else "Clear input text"
                )
            },
            trailingIcon = {
                if (!textFieldState.text.isEmpty())
                    IconButton(onClick = onSearch) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send search query"
                        )
                    }
            }
        )

        Column {
            searchResults.forEach {
                Row {
                    CenterAlignedSelectableText(it, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
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
