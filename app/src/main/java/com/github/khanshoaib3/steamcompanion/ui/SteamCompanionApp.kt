package com.github.khanshoaib3.steamcompanion.ui

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavDisplay
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavigationWrapper
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import kotlinx.coroutines.launch

@Composable
fun SteamCompanionApp() {
    val viewModel = hiltViewModel<SteamCompanionViewModel>()
    val topLevelBackStack by viewModel.topLevelBackStackState.collectAsState()
    val scope = rememberCoroutineScope()
    val view = LocalView.current

    SteamCompanionNavigationWrapper(
        currentRoute = topLevelBackStack.getLast() ?: error("Current route is null!!"),
        currentTopLevelRoute = topLevelBackStack.topLevelKey,
        navigateTo = {
            if (it.isTopLevel) topLevelBackStack.addTopLevel(it)
            else topLevelBackStack.add(it)
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        },
    ) {
        SteamCompanionNavDisplay(
            topLevelBackStack = topLevelBackStack,
            isWideScreen = isWideScreen,
            onMenuButtonClick = {
                scope.launch {
                    railState.expand()
                    view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                }
            },
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun SteamCompanionAppPreview() {
    SteamCompanionTheme {
        SteamCompanionApp()
    }
}