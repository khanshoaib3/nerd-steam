package com.github.khanshoaib3.steamcompanion.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SearchDataState(
    val pass: Boolean = true
)

data class SearchViewState(
    val pass: Boolean = true
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchRepository: SearchRepository
) : ViewModel() {
    val searchDataState: StateFlow<SearchDataState> =
        searchRepository.runSearchQuery()
            .map { searchResult ->
                SearchDataState()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = SearchDataState()
            )

    private val _searchViewState = MutableStateFlow(SearchViewState())
    val searchViewState: StateFlow<SearchViewState> = _searchViewState
}
