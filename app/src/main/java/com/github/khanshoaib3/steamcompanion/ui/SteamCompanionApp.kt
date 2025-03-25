package com.github.khanshoaib3.steamcompanion.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavHost
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavigationActions
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionNavigationWrapper
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteamCompanionApp(
    modifier: Modifier = Modifier,
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    // https://developer.android.com/develop/ui/compose/components/app-bars#center
    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
//    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

//    val foldingDevicePosture = when {
//        isBookPosture(foldingFeature) ->
//            DevicePosture.BookPosture(foldingFeature.bounds)
//
//        isSeparating(foldingFeature) ->
//            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)
//
//        else -> DevicePosture.NormalPosture
//    }

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        SteamCompanionNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()

    SteamCompanionNavigationWrapper(
        currentDestination = currentDestination,
        navigateToTopLevelDestination = { navigationActions.navigateToTopLevelRoute(it) },
    ) {
        SteamCompanionNavHost(
            navController = navController,
            menuButtonOnClick = {
                scope.launch {
                    drawerState.open()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
private fun SteamCompanionAppPreview() {
    SteamCompanionTheme {
        SteamCompanionApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
            displayFeatures = emptyList()
        )
    }
}