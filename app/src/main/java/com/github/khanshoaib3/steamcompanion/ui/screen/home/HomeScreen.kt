package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
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
    currentDestination: NavDestination?,
    navSuiteType: NavigationSuiteType,
    onMenuButtonClick: () -> Unit,
) {
    val homeDataState by homeViewModel.homeDataState.collectAsState()
    val homeViewState by homeViewModel.homeViewState.collectAsState()

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val scope = rememberCoroutineScope()
    val view = LocalView.current

    val onGameClick: (Int) -> Unit = {
        scope.launch {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Detail,
                contentKey = it
            )
        }
    }
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

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    if (navSuiteType == NavigationSuiteType.NavigationBar) {
        HomeListDetailScaffold(
            modifier = modifier,
            navSuiteType = navSuiteType,
            navigator = navigator,
            paneExpansionState = null,
            onGameClick = onGameClick,
            onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
            onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
            onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
            onMenuButtonClick = onMenuButtonClick,
            onListPaneUpButtonClick = {
                scope.launch {
                    navigator.navigateBack()
                }
            },
            homeDataState = homeDataState,
            homeViewState = homeViewState,
            topAppBarScrollBehavior = scrollBehavior,
            currentDestination = currentDestination
        )
    } else {
        // https://stackoverflow.com/a/79314221/12026423
        val paneExpansionState = rememberPaneExpansionState()
        paneExpansionState.setFirstPaneProportion(0.45f)

        Scaffold(topBar = {
            SteamCompanionTopAppBar(
                scrollBehavior = scrollBehavior,
                showMenuButton = false,
                onMenuButtonClick = onMenuButtonClick,
                currentDestination = currentDestination
            )
        }) { innerPadding ->
            HomeListDetailScaffold(
                navSuiteType = navSuiteType,
                navigator = navigator,
                paneExpansionState = paneExpansionState,
                onGameClick = onGameClick,
                onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
                onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
                onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
                onMenuButtonClick = onMenuButtonClick,
                onListPaneUpButtonClick = {},
                homeDataState = homeDataState,
                homeViewState = homeViewState,
                topAppBarScrollBehavior = scrollBehavior,
                currentDestination = currentDestination,
                modifier = modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeListDetailScaffold(
    modifier: Modifier = Modifier,
    navSuiteType: NavigationSuiteType,
    navigator: ThreePaneScaffoldNavigator<Any>,
    paneExpansionState: PaneExpansionState?,
    onGameClick: (appId: Int) -> Unit,
    onTrendingGamesCollapseButtonClick: () -> Unit,
    onTopGamesCollapseButtonClick: () -> Unit,
    onTopRecordsCollapseButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    onListPaneUpButtonClick: () -> Unit,
    homeDataState: HomeDataState,
    homeViewState: HomeViewState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    currentDestination: NavDestination?
) {
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        paneExpansionState = paneExpansionState,
        listPane = {
            AnimatedPane {
                if (navSuiteType == NavigationSuiteType.NavigationBar) {
                    HomeScreenWithScaffold(
                        onGameClick = onGameClick,
                        onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
                        onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
                        onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
                        onMenuButtonClick = onMenuButtonClick,
                        homeDataState = homeDataState,
                        homeViewState = homeViewState,
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                        currentDestination = currentDestination
                    )
                } else {
                    HomeScreen(
                        onGameClick = onGameClick,
                        onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
                        onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
                        onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
                        homeDataState = homeDataState,
                        homeViewState = homeViewState
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected item is available
                navigator.currentDestination?.contentKey.let {
                    AppDetailsScreen(
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                        appId = it as Int?,
                        showTopBar = navSuiteType == NavigationSuiteType.NavigationBar,
                        onUpButtonClick = onListPaneUpButtonClick
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithScaffold(
    modifier: Modifier = Modifier,
    onGameClick: (appId: Int) -> Unit,
    onTrendingGamesCollapseButtonClick: () -> Unit,
    onTopGamesCollapseButtonClick: () -> Unit,
    onTopRecordsCollapseButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    homeDataState: HomeDataState,
    homeViewState: HomeViewState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    currentDestination: NavDestination?
) {
    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                showMenuButton = true,
                onMenuButtonClick = onMenuButtonClick,
                currentDestination = currentDestination
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
            modifier = modifier.padding(
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = 0.dp,
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
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
