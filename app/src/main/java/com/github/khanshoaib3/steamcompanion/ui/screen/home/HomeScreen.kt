package com.github.khanshoaib3.steamcompanion.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.navigation.components.CommonTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTable
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTableType
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.ui.utils.Side
import com.github.khanshoaib3.steamcompanion.ui.utils.TopLevelRoute
import com.github.khanshoaib3.steamcompanion.ui.utils.plus
import com.github.khanshoaib3.steamcompanion.ui.utils.removePaddings
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack

// https://www.youtube.com/watch?v=W3R_ETKMj0E
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    topLevelBackStack: TopLevelBackStack<Route>,
    onMenuButtonClick: () -> Unit,
    addAppDetailPane: (Int) -> Unit,
    isWideScreen: Boolean,
    isShowingNavRail: Boolean,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val homeDataState by homeViewModel.homeDataState.collectAsState()
    val homeViewState by homeViewModel.homeViewState.collectAsState()


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

    if (isWideScreen) {
        HomeScreen(
            onGameClick = addAppDetailPane,
            onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
            onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
            onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
            homeDataState = homeDataState,
            homeViewState = homeViewState,
            modifier = modifier
        )
    } else {
        HomeScreenWithScaffold(
            onGameClick = addAppDetailPane,
            onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
            onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
            onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
            showMenuButton = !isShowingNavRail,
            onMenuButtonClick = onMenuButtonClick,
            homeDataState = homeDataState,
            homeViewState = homeViewState,
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            navigateBackCallback = { topLevelBackStack.removeLast() }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithScaffold(
    modifier: Modifier = Modifier,
    onGameClick: (Int) -> Unit,
    onTrendingGamesCollapseButtonClick: () -> Unit,
    onTopGamesCollapseButtonClick: () -> Unit,
    onTopRecordsCollapseButtonClick: () -> Unit,
    showMenuButton: Boolean,
    onMenuButtonClick: () -> Unit,
    navigateBackCallback: () -> Unit,
    homeDataState: HomeDataState,
    homeViewState: HomeViewState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                showMenuButton = showMenuButton,
                onMenuButtonClick = onMenuButtonClick,
                navigateBackCallback = navigateBackCallback,
                forRoute = TopLevelRoute.Home,
                windowInsets = WindowInsets()
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) {
        HomeScreen(
            onGameClick = onGameClick,
            onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
            onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
            onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
            homeDataState = homeDataState,
            homeViewState = homeViewState,
            modifier = modifier.padding(it.removePaddings(Side.End + Side.Start + Side.Bottom))
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
