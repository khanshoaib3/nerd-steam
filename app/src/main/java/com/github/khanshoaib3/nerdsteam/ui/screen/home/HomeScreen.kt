package com.github.khanshoaib3.nerdsteam.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.ui.components.ErrorColumn
import com.github.khanshoaib3.nerdsteam.ui.navigation.components.CommonTopAppBar
import com.github.khanshoaib3.nerdsteam.ui.screen.home.components.SteamChartsTable
import com.github.khanshoaib3.nerdsteam.ui.screen.home.components.SteamChartsTableType
import com.github.khanshoaib3.nerdsteam.ui.utils.Route
import com.github.khanshoaib3.nerdsteam.ui.utils.Side
import com.github.khanshoaib3.nerdsteam.ui.utils.TopLevelRoute
import com.github.khanshoaib3.nerdsteam.ui.utils.plus
import com.github.khanshoaib3.nerdsteam.ui.utils.removePaddings
import com.github.khanshoaib3.nerdsteam.utils.Progress
import com.github.khanshoaib3.nerdsteam.utils.TopLevelBackStack

// https://www.youtube.com/watch?v=W3R_ETKMj0E
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun HomeScreenRoot(
    topLevelBackStack: TopLevelBackStack<Route>,
    onMenuButtonClick: () -> Unit,
    addAppDetailPane: (Int) -> Unit,
    isWideScreen: Boolean,
    isShowingNavRail: Boolean,
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

    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = homeViewState.refreshStatus == Progress.LOADING,
        onRefresh = {
            homeViewModel.refresh()
            view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
        },
        state = refreshState,
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = homeViewState.refreshStatus == Progress.LOADING,
                state = refreshState,
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        if (isWideScreen) {
            HomeScreen(
                onGameClick = addAppDetailPane,
                onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
                onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
                onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
                homeDataState = homeDataState,
                homeViewState = homeViewState,
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
                topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                navigateBackCallback = { topLevelBackStack.removeLast() },
                modifier = modifier
            )
        }
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
        modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) {
        HomeScreen(
            onGameClick = onGameClick,
            onTrendingGamesCollapseButtonClick = onTrendingGamesCollapseButtonClick,
            onTopGamesCollapseButtonClick = onTopGamesCollapseButtonClick,
            onTopRecordsCollapseButtonClick = onTopRecordsCollapseButtonClick,
            homeDataState = homeDataState,
            homeViewState = homeViewState,
            modifier = Modifier.padding(it.removePaddings(Side.End + Side.Start + Side.Bottom))
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = if (homeViewState.fetchStatus is Progress.FAILED) Arrangement.Center
        else Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (homeViewState.fetchStatus is Progress.FAILED) {
            ErrorColumn(
                reason = homeViewState.fetchStatus.reason,
                titleStyle = MaterialTheme.typography.headlineMediumEmphasized,
                reasonStyle = MaterialTheme.typography.bodyMedium,
                iconSize = 40.dp
            )
        } else {
            SteamChartsTable(
                gamesList = homeDataState.trendingGames,
                tableType = SteamChartsTableType.TrendingGames,
                isTableExpanded = homeViewState.isTrendingGamesExpanded,
                isLoading = homeViewState.fetchStatus is Progress.LOADING,
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
                isLoading = homeViewState.fetchStatus is Progress.LOADING,
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
                isLoading = homeViewState.fetchStatus is Progress.LOADING,
                onCollapseButtonClick = onTopRecordsCollapseButtonClick,
                onGameRowClick = onGameClick,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .animateContentSize()
            )
        }
    }
}
