package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
        TrendingGamesRow(
            trendingGames = homeDataState.trendingGames,
            expanded = homeViewState.isTrendingGamesExpanded,
            collapseButtonOnClick = {
                homeViewModel.toggleTrendingGamesExpandState()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            },
            modifier = Modifier.animateContentSize()
        )
        TopGamesRow(
            topGames = homeDataState.topGames,
            expanded = homeViewState.isTopGamesExpanded,
            collapseButtonOnClick = {
                homeViewModel.toggleTopGamesExpandState()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            },
            modifier = Modifier.animateContentSize()
        )
        TopRecordsRow(
            homeDataState.topRecords,
            expanded = homeViewState.isTopRecordsExpanded,
            collapseButtonOnClick = {
                homeViewModel.toggleTopRecordsExpandState()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            },
            modifier = Modifier.animateContentSize()
        )
    }
}
