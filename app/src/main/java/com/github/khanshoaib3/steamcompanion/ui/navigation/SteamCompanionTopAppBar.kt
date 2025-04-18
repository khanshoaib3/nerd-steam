package com.github.khanshoaib3.steamcompanion.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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

// https://developer.android.com/develop/ui/compose/components/app-bars
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteamCompanionTopAppBar(
    modifier: Modifier = Modifier,
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (showMenuButton) {
                IconButton(onClick = onMenuButtonClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Open app drawer")
                }
            }
        },
        expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight,
        modifier = modifier
    )
}

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
                        onMenuButtonClick = {}
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