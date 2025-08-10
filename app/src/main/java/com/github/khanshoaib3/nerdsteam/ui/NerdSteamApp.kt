package com.github.khanshoaib3.nerdsteam.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.khanshoaib3.nerdsteam.ui.navigation.RootNavDisplay
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme
import com.github.khanshoaib3.nerdsteam.ui.utils.TopLevelRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NerdSteamApp() {
    val viewModel = hiltViewModel<NerdSteamViewModel>()
    val topLevelBackStack by viewModel.topLevelBackStackState.collectAsState()
    val rootBackStack = rememberNavBackStack(TopLevelRoute.Dummy)

    RootNavDisplay(
        rootBackStack = rootBackStack,
        topLevelBackStack = topLevelBackStack
    )
}

@Preview
@Composable
private fun NerdSteamAppPreview() {
    NerdSteamTheme {
        NerdSteamApp()
    }
}