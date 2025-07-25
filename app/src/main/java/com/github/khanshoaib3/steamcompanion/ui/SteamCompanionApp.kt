package com.github.khanshoaib3.steamcompanion.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.khanshoaib3.steamcompanion.ui.navigation.RootNavDisplay
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.TopLevelRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SteamCompanionApp() {
    val viewModel = hiltViewModel<SteamCompanionViewModel>()
    val topLevelBackStack by viewModel.topLevelBackStackState.collectAsState()
    val rootBackStack = rememberNavBackStack(TopLevelRoute.Dummy)

    RootNavDisplay(
        rootBackStack = rootBackStack,
        topLevelBackStack = topLevelBackStack
    )
}

@Preview
@Composable
private fun SteamCompanionAppPreview() {
    SteamCompanionTheme {
        SteamCompanionApp()
    }
}