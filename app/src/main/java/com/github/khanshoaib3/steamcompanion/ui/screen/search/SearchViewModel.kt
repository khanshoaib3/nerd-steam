package com.github.khanshoaib3.steamcompanion.ui.screen.search

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.data.model.search.AppSearchResult
import com.github.khanshoaib3.steamcompanion.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AppSearchResultDisplay(
    val appId: Int,
    val name: String,
    val iconUrl: String
)

fun AppSearchResult.toDisplayData(): AppSearchResultDisplay{
    return AppSearchResultDisplay(
        appId = appid.toInt(),
        name = name,
        iconUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/$appid/library_600x900.jpg"
    )
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    suspend fun runSearchQuery(query: String): List<AppSearchResultDisplay> {
        val result = searchRepository.runSearchQuery(query)
        return result.map { it.toDisplayData() }
    }
}
