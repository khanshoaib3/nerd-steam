package com.github.khanshoaib3.steamcompanion.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.steamcompanion.ui.navigation.Route
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavDisplay
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavigationWrapper
import com.github.khanshoaib3.steamcompanion.ui.navigation.TopLevelBackStack
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import kotlinx.coroutines.launch

@Composable
fun SteamCompanionApp() {
    val topLevelBackStack = remember { TopLevelBackStack<Any>(Route.Home) }
    val scope = rememberCoroutineScope()

    Surface {
        SteamCompanionNavigationWrapper(
            backStack = topLevelBackStack.backStack,
            navigateTo = {
                if (it.isTopLevel) topLevelBackStack.addTopLevel(it)
                else topLevelBackStack.add(it)
            },
        ) {
            SteamCompanionNavDisplay(
                topLevelBackStack = topLevelBackStack,
                navSuiteType = navSuiteType,
                onMenuButtonClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun SteamCompanionAppPreview() {
    SteamCompanionTheme {
        SteamCompanionApp()
    }
}