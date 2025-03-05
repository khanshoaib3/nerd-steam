package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.khanshoaib3.steamcompanion.ui.AppViewModelProvider
import com.github.khanshoaib3.steamcompanion.R

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        TrendingGamesRow(homeUiState.trendingGames)
        TopGamesRow(homeUiState.topGames)
        TopRecordsRow(homeUiState.topRecords)
    }
}
