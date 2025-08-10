package com.github.khanshoaib3.steamcompanion.ui.screen.search

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.data.model.api.AppSearchResponse
import com.github.khanshoaib3.steamcompanion.data.repository.SteamRepository
import com.github.khanshoaib3.steamcompanion.utils.Progress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AppSearchResultDisplay(
    val appId: Int,
    val name: String,
    val iconUrl: String,
)

fun AppSearchResponse.toDisplayData(): AppSearchResultDisplay {
    return AppSearchResultDisplay(
        appId = appid.toInt(),
        name = name,
        iconUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/$appid/library_600x900.jpg"
    )
}

data class SearchDataState(
    val searchResults: List<AppSearchResultDisplay> = listOf(),
    val searchQuery: String = "",
)

data class SearchViewState(
    val searchStatus: Progress = Progress.NOT_QUEUED,
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val steamRepository: SteamRepository,
) : ViewModel() {
    private val _searchDataState = MutableStateFlow(SearchDataState())
    val searchDataState: StateFlow<SearchDataState> = _searchDataState

    private val _searchViewState = MutableStateFlow(SearchViewState())
    val searchViewState: StateFlow<SearchViewState> = _searchViewState

    suspend fun runSearchQuery(query: String) {
        _searchViewState.update { it.copy(searchStatus = Progress.LOADING) }

        steamRepository.runSearchQuery(query).onSuccess { responses ->
            _searchDataState.update {
                it.copy(searchResults = responses.map { e -> e.toDisplayData() })
            }
            _searchViewState.update { it.copy(searchStatus = Progress.LOADED) }
        }.onFailure {throwable ->
            _searchViewState.update { it.copy(searchStatus = Progress.FAILED(throwable.message)) }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchDataState.update { it.copy(searchQuery = query) }
    }
}
