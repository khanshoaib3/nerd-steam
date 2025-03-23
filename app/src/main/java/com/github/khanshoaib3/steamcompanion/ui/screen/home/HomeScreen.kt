package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.R
import kotlinx.coroutines.launch

// https://www.youtube.com/watch?v=W3R_ETKMj0E
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeDataState by homeViewModel.homeDataState.collectAsState()
    val homeViewState by homeViewModel.homeViewState.collectAsState()

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                SteamChartsGamesList(
                    onGameClick = {
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = it
                            )
                        }
                    },
                    homeViewModel = homeViewModel,
                    homeDataState = homeDataState,
                    homeViewState = homeViewState
                )
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected item is available
                navigator.currentDestination?.contentKey.let {
                    AppDetailsScreen(
                        appId = it as Int?,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        },
        modifier = modifier
    )
}