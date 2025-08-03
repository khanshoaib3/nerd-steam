package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.CommonAppDetails
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.ITADPriceInfo
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.PlayerStatistics
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.PriceTracking
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.toITADPriceInfo
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.toPlayerStatistics
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.repository.AppDetailRepository
import com.github.khanshoaib3.steamcompanion.data.repository.BookmarkRepository
import com.github.khanshoaib3.steamcompanion.data.repository.IsThereAnyDealRepository
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsPerAppScraper
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.Progress.FAILED
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.Progress.LOADED
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.Progress.LOADING
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.Progress.NOT_QUEUED
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

enum class DataType {
    COMMON_APP_DETAILS, IS_THERE_ANY_DEAL_PRICE_INFO, STEAM_CHARTS_PLAYER_STATS
}

data class AppData(
    val steamAppId: Int,
    val isThereAnyDealId: String? = null,
    val commonDetails: CommonAppDetails? = null,
    val isBookmarked: Boolean = false,
    val priceTrackingInfo: PriceTracking? = null,
    val isThereAnyDealPriceInfo: ITADPriceInfo? = null,
    val playerStatistics: PlayerStatistics? = null,
)

data class AppViewState(
    val selectedTabIndex: Int = 0,
    val steamChartsStatus: Progress = NOT_QUEUED,
    val isThereAnyDealPriceInfoStatus: Progress = NOT_QUEUED,
    val steamStatus: Progress = NOT_QUEUED,
    val isThereAnyDealGameInfoStatus: Progress = NOT_QUEUED,
)

@HiltViewModel(assistedFactory = AppDetailViewModel.Factory::class)
class AppDetailViewModel @AssistedInject constructor(
    private val appDetailRepository: AppDetailRepository,
    private val isThereAnyDealRepository: IsThereAnyDealRepository,
    private val bookmarkRepository: BookmarkRepository,
    @Assisted val key: Route.AppDetail,
) : ViewModel() {
    private val _appData = MutableStateFlow(AppData(key.appId))
    val appData: StateFlow<AppData> = _appData

    private val _viewState = MutableStateFlow(AppViewState())
    val appViewState: StateFlow<AppViewState> = _viewState

    init {
        fetchDataFromSource(DataType.COMMON_APP_DETAILS)
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: Route.AppDetail): AppDetailViewModel
    }

    fun fetchDataFromSource(dataType: DataType) =
        viewModelScope.launch(Dispatchers.IO) {
            when (dataType) {
                DataType.COMMON_APP_DETAILS -> fetchCommonAppDetails()
                DataType.STEAM_CHARTS_PLAYER_STATS -> fetchSteamChartsPlayerStats()
                DataType.IS_THERE_ANY_DEAL_PRICE_INFO -> fetchISTDPriceInfo()
            }
        }

    suspend fun fetchCommonAppDetails() {
        if (appViewState.value.steamStatus != NOT_QUEUED
            && appViewState.value.isThereAnyDealGameInfoStatus != NOT_QUEUED
        ) return

        _viewState.update {
            it.copy(steamStatus = LOADING, isThereAnyDealGameInfoStatus = LOADING)
        }

        val steamResponse = appDetailRepository.fetchDataForAppId(key.appId)
        val isThereAnyDealResponse =
            isThereAnyDealRepository.getGameInfoFromAppId(key.appId)

        if (((steamResponse != null && !steamResponse.success) || steamResponse == null)
            && isThereAnyDealResponse.isFailure
        ) {
            _viewState.update {
                it.copy(
                    steamStatus = FAILED,
                    isThereAnyDealGameInfoStatus = FAILED
                )
            }
            return
        }

        if ((steamResponse != null && !steamResponse.success) || steamResponse == null) {
            _viewState.update { it.copy(steamStatus = FAILED) }
        }

        if (isThereAnyDealResponse.isFailure) {
            _viewState.update { it.copy(isThereAnyDealGameInfoStatus = FAILED) }
        }

        _appData.update {
            it.copy(
                commonDetails = CommonAppDetails.fromIsThereAnyDealAndSteam(
                    _steamResponse = steamResponse,
                    _isThereAnyDealResponse = isThereAnyDealResponse.getOrNull()
                ),
                isThereAnyDealId = isThereAnyDealResponse.getOrNull()?.id,
                isBookmarked = bookmarkRepository.isBookmarked(key.appId)
            )
        }

        _viewState.update {
            it.copy(
                steamStatus = if (it.steamStatus == FAILED) FAILED else LOADED,
                isThereAnyDealGameInfoStatus = if (it.isThereAnyDealGameInfoStatus == FAILED) FAILED else LOADED,
            )
        }
    }

    suspend fun fetchSteamChartsPlayerStats() {
        if (appViewState.value.steamChartsStatus != NOT_QUEUED) return

        _viewState.update { it.copy(steamChartsStatus = LOADING) }
        try {
            _appData.update {
                it.copy(
                    playerStatistics = SteamChartsPerAppScraper(key.appId).scrape()
                        .toPlayerStatistics()
                )
            }
            _viewState.update { it.copy(steamChartsStatus = LOADED) }
        } catch (_: Exception) {
            _viewState.update { it.copy(steamChartsStatus = FAILED) }
        }
    }

    suspend fun fetchISTDPriceInfo() {
        if (appViewState.value.isThereAnyDealPriceInfoStatus != NOT_QUEUED) return

        _viewState.update { it.copy(steamChartsStatus = LOADING) }

        if (appData.value.isThereAnyDealId == null) {
            isThereAnyDealRepository.lookupGame(appId = appData.value.steamAppId)
                .onSuccess { response ->
                    _appData.update { it.copy(isThereAnyDealId = response.id) }
                }
                .onFailure {
                    _viewState.update { it.copy(isThereAnyDealPriceInfoStatus = FAILED) }
                    return
                }
        }

        isThereAnyDealRepository.getPriceInfo(appData.value.isThereAnyDealId!!)
            .onSuccess { response ->
                _appData.update { it.copy(isThereAnyDealPriceInfo = response.toITADPriceInfo()) }
                _viewState.update { it.copy(steamChartsStatus = LOADED) }
            }
            .onFailure {
                _viewState.update { it.copy(isThereAnyDealPriceInfoStatus = FAILED) }
                return
            }
    }

    suspend fun toggleBookmarkStatus() = appData.value.apply {
        if (appData.value.commonDetails == null) return@apply

        if (!isBookmarked) {
            bookmarkRepository.addBookmark(
                Bookmark(
                    appId = key.appId,
                    name = appData.value.commonDetails!!.name,
                )
            )
            _appData.update { it.copy(isBookmarked = true) }
        } else {
            bookmarkRepository.removeBookmark(key.appId)
            _appData.update { it.copy(isBookmarked = false) }
        }
    }

    suspend fun getPriceTrackingInfo(): PriceTracking? {
        return appDetailRepository.getPriceTrackingInfo(appId = key.appId)
    }

    suspend fun startPriceTracking(targetPrice: Float, notifyEveryDay: Boolean) =
        appData.value.commonDetails?.let {
            appDetailRepository.trackPrice(
                PriceTracking(
                    appId = key.appId,
                    gameName = appData.value.commonDetails!!.name,
                    targetPrice = targetPrice,
                    notifyEveryDay = notifyEveryDay,
                )
            )
        }

    suspend fun stopPriceTracking() {
        appDetailRepository.stopTracking(appId = key.appId)
    }
}
