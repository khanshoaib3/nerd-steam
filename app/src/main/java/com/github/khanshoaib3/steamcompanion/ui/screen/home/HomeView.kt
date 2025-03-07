package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.khanshoaib3.steamcompanion.ui.AppViewModelProvider
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTable
import com.github.khanshoaib3.steamcompanion.ui.screen.home.components.SteamChartsTableType

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeDataState by homeViewModel.homeDataState.collectAsState()
    val homeViewState by homeViewModel.homeViewState.collectAsState()

    val view = LocalView.current

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        SteamChartsTable(
            gamesList = homeDataState.trendingGames,
            tableType = SteamChartsTableType.TrendingGames,
            isTableExpanded = homeViewState.isTrendingGamesExpanded,
            collapseButtonOnClick = {
                homeViewModel.toggleTrendingGamesExpandState()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .animateContentSize()
        )
        SteamChartsTable(
            gamesList = homeDataState.topGames,
            tableType = SteamChartsTableType.TopGames,
            isTableExpanded = homeViewState.isTopGamesExpanded,
            collapseButtonOnClick = {
                homeViewModel.toggleTopGamesExpandState()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .animateContentSize()
        )
        SteamChartsTable(
            gamesList = homeDataState.topRecords,
            tableType = SteamChartsTableType.TopRecords,
            isTableExpanded = homeViewState.isTopRecordsExpanded,
            collapseButtonOnClick = {
                homeViewModel.toggleTopRecordsExpandState()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .animateContentSize()
        )
    }
}
