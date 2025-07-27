package com.github.khanshoaib3.steamcompanion.ui.screen.detail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.model.detail.PriceTracking
import com.github.khanshoaib3.steamcompanion.data.model.detail.SteamWebApiAppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.repository.BookmarkRepository
import com.github.khanshoaib3.steamcompanion.data.repository.AppDetailRepository
import com.github.khanshoaib3.steamcompanion.data.scraper.PlayerStatsRowData
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsPerAppScraper
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Progress {
    NOT_QUEUED, LOADING, LOADED, FAILED
}

enum class DataSourceType {
    STEAM, IS_THERE_ANY_DEAL, STEAM_CHARTS
}

data class AppData(
    val content: SteamWebApiAppDetailsResponse? = null,
    val isBookmarked: Boolean = false,
    val playerStatsRowData: List<PlayerStatsRowData> = listOf(),
)

data class AppViewState(
    val selectedTabIndex: Int = 0,
    val steamChartsFetchStatus: Progress = Progress.NOT_QUEUED,
)

@HiltViewModel(assistedFactory = AppDetailViewModel.Factory::class)
class AppDetailViewModel @AssistedInject constructor(
    private val appDetailRepository: AppDetailRepository,
    private val bookmarkRepository: BookmarkRepository,
    @Assisted val key: Route.AppDetail,
) : ViewModel() {
    private val _appData = MutableStateFlow(AppData())
    val appData: StateFlow<AppData> = _appData

    private val _appViewState = MutableStateFlow(AppViewState())
    val appViewState: StateFlow<AppViewState> = _appViewState

    @AssistedFactory
    interface Factory {
        fun create(navKey: Route.AppDetail): AppDetailViewModel
    }

    fun fetchDataFromSource(dataSourceType: DataSourceType) {
        when (dataSourceType) {
            DataSourceType.STEAM_CHARTS -> if (appViewState.value.steamChartsFetchStatus == Progress.NOT_QUEUED) {
                viewModelScope.launch(Dispatchers.IO) {
                    fetchSteamChartsData()
                }
            }

            else -> {}
        }
    }

    suspend fun updateAppId() {
        val realAppId = key.appId
        if (realAppId == null) return
        if (realAppId == 0) return

        _appData.update {
            val content = appDetailRepository.fetchDataForAppId(appId = realAppId)
            val isBookmarked = bookmarkRepository.isBookmarked(content?.data?.steamAppId)
            AppData(
                content = content,
                isBookmarked = isBookmarked,
//                playerStatsRows = SteamChartsPerAppScraper(realAppId).scrape().playerStatsRows
            )
        }
    }

    suspend fun fetchSteamChartsData() {
        if (appViewState.value.steamChartsFetchStatus != Progress.NOT_QUEUED) return
        if (key.appId == null) return
        if (key.appId == 0) return

        _appViewState.update { it.copy(steamChartsFetchStatus = Progress.LOADING) }
        try {
            _appData.update {
                it.copy(playerStatsRowData = SteamChartsPerAppScraper(key.appId).scrape().playerStatsRowData)
            }
            _appViewState.update { it.copy(steamChartsFetchStatus = Progress.LOADED) }
        } catch (_: Exception) {
            _appViewState.update { it.copy(steamChartsFetchStatus = Progress.FAILED) }
        }
    }

    suspend fun toggleBookmarkStatus() {
        appData.value.apply {
            if (content?.data?.steamAppId == null) return

            if (!isBookmarked) {
                bookmarkRepository.addBookmark(
                    Bookmark(
                        appId = content.data.steamAppId,
                        name = content.data.name,
                    )
                )
                _appData.update {
                    it.copy(
                        isBookmarked = true
                    )
                }
            } else {
                bookmarkRepository.removeBookmark(content.data.steamAppId)
                _appData.update {
                    it.copy(
                        isBookmarked = false
                    )
                }
            }
        }
    }

    suspend fun getPriceTrackingInfo(): PriceTracking? {
        if (appData.value.content?.data?.steamAppId == null) return null

        return appDetailRepository.getPriceTrackingInfo(appId = appData.value.content!!.data!!.steamAppId)
    }

    suspend fun startPriceTracking(targetPrice: Float, notifyEveryDay: Boolean) {
        if (appData.value.content?.data?.steamAppId == null) return

        appDetailRepository.trackPrice(
            PriceTracking(
                appId = appData.value.content!!.data!!.steamAppId,
                gameName = appData.value.content!!.data!!.name,
                targetPrice = targetPrice,
                notifyEveryDay = notifyEveryDay,
            )
        )
    }

    suspend fun stopPriceTracking() {
        if (appData.value.content?.data?.steamAppId == null) return

        appDetailRepository.stopTracking(appId = appData.value.content!!.data!!.steamAppId)
    }
}
