package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailsScreen
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTable
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTableType
import kotlinx.coroutines.launch

// https://www.youtube.com/watch?v=W3R_ETKMj0E
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMenuButtonClick: () -> Unit,
) {
    val homeDataState by homeViewModel.homeDataState.collectAsState()
    val homeViewState by homeViewModel.homeViewState.collectAsState()

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val scope = rememberCoroutineScope()
    val view = LocalView.current


    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                HomeScreen(
                    onGameClick = {
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = it
                            )
                        }
                    },
                    onTrendingGamesCollapseButtonClick = {
                        homeViewModel.toggleTrendingGamesExpandState()
                        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
                    },
                    onTopGamesCollapseButtonClick = {
                        homeViewModel.toggleTopGamesExpandState()
                        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
                    },
                    onTopRecordsCollapseButtonClick = {
                        homeViewModel.toggleTopRecordsExpandState()
                        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
                    },
                    onMenuButtonClick = onMenuButtonClick,
                    homeDataState = homeDataState,
                    homeViewState = homeViewState,
                    topAppBarScrollBehavior = scrollBehavior,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected item is available
                navigator.currentDestination?.contentKey.let {
                    AppDetailsScreen(
                        appId = it as Int?,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onGameClick: (appId: Int) -> Unit,
    onTrendingGamesCollapseButtonClick: () -> Unit,
    onTopGamesCollapseButtonClick: () -> Unit,
    onTopRecordsCollapseButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    homeDataState: HomeDataState,
    homeViewState: HomeViewState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                onMenuButtonClick = onMenuButtonClick
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SteamChartsTable(
                gamesList = homeDataState.trendingGames,
                tableType = SteamChartsTableType.TrendingGames,
                isTableExpanded = homeViewState.isTrendingGamesExpanded,
                onCollapseButtonClick = onTrendingGamesCollapseButtonClick,
                onGameRowClick = onGameClick,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .animateContentSize()
            )
            SteamChartsTable(
                gamesList = homeDataState.topGames,
                tableType = SteamChartsTableType.TopGames,
                isTableExpanded = homeViewState.isTopGamesExpanded,
                onCollapseButtonClick = onTopGamesCollapseButtonClick,
                onGameRowClick = onGameClick,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .animateContentSize()
            )
            SteamChartsTable(
                gamesList = homeDataState.topRecords,
                tableType = SteamChartsTableType.TopRecords,
                isTableExpanded = homeViewState.isTopRecordsExpanded,
                onCollapseButtonClick = onTopRecordsCollapseButtonClick,
                onGameRowClick = onGameClick,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .animateContentSize()
            )
        }
    }
}
