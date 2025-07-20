package com.github.khanshoaib3.steamcompanion.ui.screen.home

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
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTable
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTableType
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.Side
import com.github.khanshoaib3.steamcompanion.ui.utils.plus
import com.github.khanshoaib3.steamcompanion.ui.utils.removeBottomPadding
import com.github.khanshoaib3.steamcompanion.ui.utils.removePaddings
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack

// https://www.youtube.com/watch?v=W3R_ETKMj0E
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    topLevelBackStack: TopLevelBackStack<Route>,
    navSuiteType: NavigationSuiteType,
    onMenuButtonClick: () -> Unit,
    addAppDetailPane: (Int) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeDataState by homeViewModel.homeDataState.collectAsState()
    val homeViewState by homeViewModel.homeViewState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val view = LocalView.current

    val onTrendingGamesCollapseButtonClick: () -> Unit = {
        homeViewModel.toggleTrendingGamesExpandState()
        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
    }
    val onTopGamesCollapseButtonClick: () -> Unit = {
        homeViewModel.toggleTopGamesExpandState()
        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
    }
    val onTopRecordsCollapseButtonClick: () -> Unit = {
        homeViewModel.toggleTopRecordsExpandState()
        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
    }

    HomeScreenWithScaffold(
        modifier = modifier,
        navSuiteType = navSuiteType,
        onGameClick = addAppDetailPane,
        onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
        onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
        onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
        showMenuButton = navSuiteType == NavigationSuiteType.NavigationBar,
        onMenuButtonClick = onMenuButtonClick,
        homeDataState = homeDataState,
        homeViewState = homeViewState,
        topAppBarScrollBehavior = scrollBehavior,
        backStack = topLevelBackStack.backStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithScaffold(
    modifier: Modifier = Modifier,
    navSuiteType: NavigationSuiteType,
    onGameClick: (Int) -> Unit,
    onTrendingGamesCollapseButtonClick: () -> Unit,
    onTopGamesCollapseButtonClick: () -> Unit,
    onTopRecordsCollapseButtonClick: () -> Unit,
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    homeDataState: HomeDataState,
    homeViewState: HomeViewState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    backStack: SnapshotStateList<Route>
) {
    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                showMenuButton = showMenuButton,
                onMenuButtonClick = onMenuButtonClick,
                backStack = backStack
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        HomeScreen(
            onGameClick = onGameClick,
            onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
            onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
            onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
            homeDataState = homeDataState,
            homeViewState = homeViewState,
            modifier = modifier
                .padding(
                    if (navSuiteType == NavigationSuiteType.NavigationBar) innerPadding.removeBottomPadding()
                    else innerPadding.removePaddings(Side.End + Side.Start + Side.End)
                )
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onGameClick: (appId: Int) -> Unit,
    onTrendingGamesCollapseButtonClick: () -> Unit,
    onTopGamesCollapseButtonClick: () -> Unit,
    onTopRecordsCollapseButtonClick: () -> Unit,
    homeDataState: HomeDataState,
    homeViewState: HomeViewState,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier.verticalScroll(rememberScrollState())
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
