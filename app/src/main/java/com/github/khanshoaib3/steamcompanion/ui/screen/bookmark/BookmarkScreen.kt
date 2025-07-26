package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.ui.navigation.components.CommonTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.components.BookmarkTable
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.Side
import com.github.khanshoaib3.steamcompanion.ui.utils.plus
import com.github.khanshoaib3.steamcompanion.ui.utils.removePaddings

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreenRoot(
    navigateBackCallback: () -> Unit,
    addAppDetailPane: (Int) -> Unit,
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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val onGameHeaderClick: () -> Unit = {
        bookmarkViewModel.toggleSortOrderOfTypeName()
        localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }
    val onTimeHeaderClick: () -> Unit = {
        bookmarkViewModel.toggleSortOrderOfTypeTime()
        localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                scrollBehavior = scrollBehavior,
                showMenuButton = false,
                onMenuButtonClick = {},
                navigateBackCallback = navigateBackCallback,
                forRoute = Route.Bookmark,
                windowInsets = WindowInsets()
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        BookmarkScreen(
            bookmarks = sortedBookmarks,
            onGameClick = addAppDetailPane,
            onGameHeaderClick = onGameHeaderClick,
            onTimeHeaderClick = onTimeHeaderClick,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
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
        val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            topBar = {
                CommonTopAppBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    showMenuButton = true,
                    onMenuButtonClick = {},
                    navigateBackCallback = {},
                    forRoute = Route.Bookmark
                )
            },
            modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            BookmarkScreen(
                bookmarks = listOf(
                    BookmarkDisplay(
                        appId = 1231,
                        name = "Max Payne: The Fall of Max Payne",
                        formattedTime = "dd MMM yyyy"
                    )
                ),
                onGameClick = { it: Int -> },
                onGameHeaderClick = {},
                onTimeHeaderClick = {},
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                modifier = Modifier
                    .padding(innerPadding.removePaddings(Side.End + Side.Start + Side.Bottom))
            )
        }
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