package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.steamcompanion.data.repository.SteamChartsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val trendingGames: List<TrendingGame>,
    val topGames: List<TopGame>,
    val topRecords: List<TopRecord>,
)

class HomeViewModel(
    private val steamChartsRepository: SteamChartsRepository,
    private val localDataStoreRepository: LocalDataStoreRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            steamChartsRepository.fetchAndStoreData()
        }
    }

    val homeUiState: StateFlow<HomeUiState> =
        combine(
            steamChartsRepository.getAllTrendingGames(),
            steamChartsRepository.getAllTopGames(),
            steamChartsRepository.getAllTopRecords()
        ) { trendingGames, topGames, topRecords ->
            HomeUiState(
                trendingGames = trendingGames,
                topGames = topGames,
                topRecords = topRecords
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = HomeUiState(listOf(), listOf(), listOf())
            )
}