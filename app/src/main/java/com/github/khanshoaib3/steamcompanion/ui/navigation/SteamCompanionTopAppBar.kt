package com.github.khanshoaib3.steamcompanion.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.Route

// https://developer.android.com/develop/ui/compose/components/app-bars
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteamCompanionTopAppBar(
    modifier: Modifier = Modifier,
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    backStack: SnapshotStateList<Any>
) {
    val title: String = when (backStack.lastOrNull() as Route) {
        Route.Home -> stringResource(R.string.app_name)
        Route.Search -> "Search"
        Route.Bookmark -> "Bookmark"
        else -> stringResource(R.string.app_name)
    }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (backStack.lastOrNull() is Route.Bookmark) {
                IconButton(onClick = { backStack.removeLastOrNull() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close page")
                }
            } else if (showMenuButton) {
                IconButton(onClick = onMenuButtonClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Open app drawer")
                }
            }
        },
        expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight,
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopAppBarPreview() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    SteamCompanionTheme {
        Surface {
            Scaffold(
                topBar = {
                    SteamCompanionTopAppBar(
                        scrollBehavior = scrollBehavior,
                        showMenuButton = true,
                        onMenuButtonClick = {},
                        backStack = mutableStateListOf(Route.Home)
                    )
                }
            ) {
                CenterAlignedSelectableText(
                    "Some text",
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                )
            }
        }
    }
}