package com.github.khanshoaib3.steamcompanion.ui.screen.search

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.data.model.api.AppSearchResponse
import com.github.khanshoaib3.steamcompanion.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AppSearchResultDisplay(
    val appId: Int,
    val name: String,
    val iconUrl: String
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

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchDataState = MutableStateFlow(SearchDataState())
    val searchDataStateFlow: StateFlow<SearchDataState> = _searchDataState

    suspend fun runSearchQuery(query: String){
        val result = searchRepository.runSearchQuery(query).map { it.toDisplayData() }
        _searchDataState.update {
            it.copy(
                searchResults = result
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _searchDataState.update { it.copy(searchQuery = query) }
    }
}
