package com.github.khanshoaib3.nerdsteam.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TopGame
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.nerdsteam.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.nerdsteam.data.repository.SteamChartsRepository
import com.github.khanshoaib3.nerdsteam.utils.Progress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeDataState(
    val trendingGames: List<TrendingGame>,
    val topGames: List<TopGame>,
    val topRecords: List<TopRecord>,
)

data class HomeViewState(
    val fetchStatus: Progress = Progress.NOT_QUEUED,
    val isTrendingGamesExpanded: Boolean = true,
    val isTopGamesExpanded: Boolean = true,
    val isTopRecordsExpanded: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val steamChartsRepository: SteamChartsRepository
) : ViewModel() {
    val homeDataState: StateFlow<HomeDataState> = combine(
        steamChartsRepository.getAllTrendingGames(),
        steamChartsRepository.getAllTopGames(),
        steamChartsRepository.getAllTopRecords()
    ) { trendingGames, topGames, topRecords ->
        HomeDataState(
            trendingGames = trendingGames, topGames = topGames, topRecords = topRecords
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeDataState(listOf(), listOf(), listOf())
    )

    private val _homeViewState = MutableStateFlow(HomeViewState())
    val homeViewState: StateFlow<HomeViewState> = _homeViewState

    init {
        // https://stackoverflow.com/a/70574508/12026423
        viewModelScope.launch(Dispatchers.IO) {
            _homeViewState.update { it.copy(fetchStatus = Progress.LOADING) }

            steamChartsRepository.fetchAndStoreData()
                .onSuccess { _homeViewState.update { it.copy(fetchStatus = Progress.LOADED) } }
                .onFailure { throwable ->
                    _homeViewState.update { it.copy(fetchStatus = Progress.FAILED(throwable.message)) }
                }
        }
    }

    fun toggleTrendingGamesExpandState() {
        _homeViewState.value =
            _homeViewState.value.copy(isTrendingGamesExpanded = !_homeViewState.value.isTrendingGamesExpanded)
    }

    fun toggleTopGamesExpandState() {
        _homeViewState.value =
            _homeViewState.value.copy(isTopGamesExpanded = !_homeViewState.value.isTopGamesExpanded)
    }

    fun toggleTopRecordsExpandState() {
        _homeViewState.value =
            _homeViewState.value.copy(isTopRecordsExpanded = !_homeViewState.value.isTopRecordsExpanded)
    }
}