package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.CommonAppDetails
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.Dlc
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.ITADPriceInfo
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PlayerStatistics
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.toITADPriceInfo
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.toPlayerStatistics
import com.github.khanshoaib3.nerdsteam.data.model.bookmark.Bookmark
import com.github.khanshoaib3.nerdsteam.data.repository.BookmarkRepository
import com.github.khanshoaib3.nerdsteam.data.repository.IsThereAnyDealRepository
import com.github.khanshoaib3.nerdsteam.data.repository.PriceAlertRepository
import com.github.khanshoaib3.nerdsteam.data.repository.SteamChartsRepository
import com.github.khanshoaib3.nerdsteam.data.repository.SteamRepository
import com.github.khanshoaib3.nerdsteam.ui.utils.Route
import com.github.khanshoaib3.nerdsteam.utils.Progress
import com.github.khanshoaib3.nerdsteam.utils.Progress.FAILED
import com.github.khanshoaib3.nerdsteam.utils.Progress.LOADED
import com.github.khanshoaib3.nerdsteam.utils.Progress.LOADING
import com.github.khanshoaib3.nerdsteam.utils.Progress.NOT_QUEUED
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class DataType {
    COMMON_APP_DETAILS, IS_THERE_ANY_DEAL_PRICE_INFO, STEAM_CHARTS_PLAYER_STATS, DLCS
}

data class AppData(
    val steamAppId: Int,
    val isThereAnyDealId: String? = null,
    val isThereAnyDealSlug: String? = null,
    val commonDetails: CommonAppDetails? = null,
    val isBookmarked: Boolean = false,
    val priceAlertInfo: PriceAlert? = null,
    val isThereAnyDealPriceInfo: ITADPriceInfo? = null,
    val playerStatistics: PlayerStatistics? = null,
    val dlcs: List<Dlc>? = null,
)

data class AppViewState(
    val selectedTabIndex: Int = 0,
    val steamStatus: Progress = NOT_QUEUED,
    val isThereAnyDealGameInfoStatus: Progress = NOT_QUEUED,
    val steamChartsStatus: Progress = NOT_QUEUED,
    val isThereAnyDealPriceInfoStatus: Progress = NOT_QUEUED,
    val dlcsStatus: Progress = NOT_QUEUED,
)

@HiltViewModel(assistedFactory = AppDetailViewModel.Factory::class)
class AppDetailViewModel @AssistedInject constructor(
    private val steamRepository: SteamRepository,
    private val steamChartsRepository: SteamChartsRepository,
    private val isThereAnyDealRepository: IsThereAnyDealRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val priceAlertRepository: PriceAlertRepository,
    @Assisted val key: Route.AppDetail,
) : ViewModel() {
    private val _appData = MutableStateFlow(AppData(key.appId))
    val appData: StateFlow<AppData> = _appData

    private val _appViewState = MutableStateFlow(AppViewState())
    val appViewState: StateFlow<AppViewState> = _appViewState

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
                DataType.DLCS -> fetchDlcs()
            }
        }

    suspend fun fetchCommonAppDetails() {
        if (appViewState.value.steamStatus != NOT_QUEUED
            && appViewState.value.isThereAnyDealGameInfoStatus != NOT_QUEUED
        ) return

        _appViewState.update {
            it.copy(steamStatus = LOADING, isThereAnyDealGameInfoStatus = LOADING)
        }

        val steamResponse = steamRepository.fetchDataForAppId(key.appId)
        val isThereAnyDealResponse =
            isThereAnyDealRepository.getGameInfoFromAppId(key.appId)

        if (steamResponse.isFailure && isThereAnyDealResponse.isFailure) {
            _appViewState.update {
                it.copy(
                    steamStatus = FAILED("Unable to fetch data from steam"),
                    isThereAnyDealGameInfoStatus = FAILED(steamResponse.exceptionOrNull()?.message)
                )
            }
            return
        }

        if (steamResponse.isFailure) {
            _appViewState.update { it.copy(steamStatus = FAILED("Unable to fetch data from steam")) }
        }

        if (isThereAnyDealResponse.isFailure) {
            _appViewState.update {
                it.copy(isThereAnyDealGameInfoStatus = FAILED(isThereAnyDealResponse.exceptionOrNull()?.message))
            }
        }

        _appData.update {
            it.copy(
                commonDetails = CommonAppDetails.fromIsThereAnyDealAndSteam(
                    steamResponse = steamResponse.getOrNull(),
                    isThereAnyDealResponse = isThereAnyDealResponse.getOrNull()
                ),
                isThereAnyDealId = isThereAnyDealResponse.getOrNull()?.id,
                isThereAnyDealSlug = isThereAnyDealResponse.getOrNull()?.slug,
                isBookmarked = bookmarkRepository.isBookmarked(key.appId)
            )
        }

        _appViewState.update {
            it.copy(
                steamStatus = if (it.steamStatus is FAILED) FAILED("Unable to fetch data from steam") else LOADED,
                isThereAnyDealGameInfoStatus = if (it.isThereAnyDealGameInfoStatus is FAILED)
                    FAILED(isThereAnyDealResponse.exceptionOrNull()?.message) else LOADED,
            )
        }
    }

    suspend fun fetchSteamChartsPlayerStats() {
        if (appViewState.value.steamChartsStatus != NOT_QUEUED) return

        _appViewState.update { it.copy(steamChartsStatus = LOADING) }

        steamChartsRepository.fetchDataForApp(key.appId)
            .onSuccess { result ->
                _appData.update { it.copy(playerStatistics = result.toPlayerStatistics()) }
                _appViewState.update { it.copy(steamChartsStatus = LOADED) }
            }
            .onFailure { throwable ->
                _appViewState.update { it.copy(steamChartsStatus = FAILED(throwable.message)) }
            }
    }

    suspend fun fetchISTDPriceInfo() {
        if (appViewState.value.isThereAnyDealPriceInfoStatus != NOT_QUEUED) return

        _appViewState.update { it.copy(isThereAnyDealPriceInfoStatus = LOADING) }

        if (appData.value.isThereAnyDealId == null) {
            isThereAnyDealRepository.lookupGame(appId = appData.value.steamAppId)
                .onSuccess { response ->
                    _appData.update { it.copy(isThereAnyDealId = response.id) }
                }
                .onFailure { throwable ->
                    _appViewState.update { it.copy(isThereAnyDealPriceInfoStatus = FAILED(throwable.message)) }
                    return
                }
        }

        isThereAnyDealRepository.getPriceInfo(appData.value.isThereAnyDealId!!)
            .onSuccess { response ->
                _appData.update { it.copy(isThereAnyDealPriceInfo = response.toITADPriceInfo()) }
                _appViewState.update { it.copy(isThereAnyDealPriceInfoStatus = LOADED) }
            }
            .onFailure { throwable ->
                _appViewState.update { it.copy(isThereAnyDealPriceInfoStatus = FAILED(throwable.message)) }
                return
            }
    }

    suspend fun fetchDlcs() {
        if (appViewState.value.dlcsStatus != NOT_QUEUED) return
        if (appData.value.commonDetails?.dlcIds.isNullOrEmpty()){
            _appViewState.update { it.copy(dlcsStatus = FAILED("No DLCs found!")) }
            return
        }

        _appViewState.update { it.copy(dlcsStatus = LOADING) }
        steamRepository.fetchDataForDlcs(appData.value.commonDetails!!.dlcIds!!)
            .onSuccess { result ->
                _appData.update { it.copy(dlcs = result) }
                _appViewState.update { it.copy(dlcsStatus = LOADED) }
            }
            .onFailure { throwable ->
                _appViewState.update { it.copy(dlcsStatus = FAILED(throwable.message)) }
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

    suspend fun getPriceTrackingInfo(): PriceAlert? {
        return priceAlertRepository.getPriceAlert(appId = key.appId)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun startPriceTracking(targetPrice: Float, notifyEveryDay: Boolean) =
        appData.value.commonDetails?.let {
            priceAlertRepository.addPriceAlert(
                PriceAlert(
                    appId = key.appId,
                    name = it.name,
                    targetPrice = targetPrice,
                    notifyEveryDay = notifyEveryDay,
                    lastFetchedTime = Clock.System.now().toEpochMilliseconds(),
                    lastFetchedPrice = it.currentPrice,
                    originalPrice = it.originalPrice,
                    currency = it.currency,
                )
            )
        }

    suspend fun stopPriceTracking() {
        priceAlertRepository.removePriceAlert(appId = key.appId)
    }

    fun updateSelectedTabIndex(index: Int) {
        _appViewState.update { it.copy(selectedTabIndex = index) }
    }
}
