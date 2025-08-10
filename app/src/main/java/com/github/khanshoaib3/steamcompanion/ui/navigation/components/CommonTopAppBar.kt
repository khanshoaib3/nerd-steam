package com.github.khanshoaib3.steamcompanion.ui.navigation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.TopLevelRoute

// https://developer.android.com/develop/ui/compose/components/app-bars
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    navigateBackCallback: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    forRoute: Route,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    val title: String = when (forRoute) {
        is TopLevelRoute -> stringResource(R.string.app_name)
        Route.Bookmark -> "Bookmarks"
        Route.Alerts -> "Price Alerts"
        else -> forRoute.name
    }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (forRoute !is TopLevelRoute) {
                IconButton(onClick = navigateBackCallback) {
                    Icon(Icons.Default.Close, contentDescription = "Close page")
                }
            } else if (showMenuButton) {
                IconButton(onClick = onMenuButtonClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Open app drawer")
                }
            }
        },
        windowInsets = windowInsets,
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
                    CommonTopAppBar(
                        scrollBehavior = scrollBehavior,
                        showMenuButton = true,
                        onMenuButtonClick = {},
                        navigateBackCallback = {},
                        forRoute = Route.Bookmark
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