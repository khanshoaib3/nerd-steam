package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import android.util.Log
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
import com.github.khanshoaib3.nerdsteam.data.repository.CacheRepository
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TAG = "AppDetailViewModel"

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

@Serializable
data class SerializableAppData(
    val steamAppId: Int,
    val isThereAnyDealId: String?,
    val isThereAnyDealSlug: String?,
    val commonDetails: CommonAppDetails?,
    val isThereAnyDealPriceInfo: ITADPriceInfo?,
    val dlcs: List<Dlc>?,
)

fun AppData.toSerializableAppData() = SerializableAppData(
    steamAppId = this.steamAppId,
    isThereAnyDealId = this.isThereAnyDealId,
    isThereAnyDealSlug = this.isThereAnyDealSlug,
    commonDetails = this.commonDetails,
    isThereAnyDealPriceInfo = this.isThereAnyDealPriceInfo,
    dlcs = this.dlcs,
)

fun SerializableAppData.toAppData() = AppData(
    steamAppId = this.steamAppId,
    isThereAnyDealId = this.isThereAnyDealId,
    isThereAnyDealSlug = this.isThereAnyDealSlug,
    commonDetails = this.commonDetails,
    isThereAnyDealPriceInfo = this.isThereAnyDealPriceInfo,
    dlcs = this.dlcs,
)

data class AppViewState(
    val selectedTabIndex: Int = 0,
    val steamStatus: Progress = NOT_QUEUED,
    val isThereAnyDealGameInfoStatus: Progress = NOT_QUEUED,
    val steamChartsStatus: Progress = NOT_QUEUED,
    val isThereAnyDealPriceInfoStatus: Progress = NOT_QUEUED,
    val dlcsStatus: Progress = NOT_QUEUED,
    val refreshStatus: Progress = NOT_QUEUED,
)

@HiltViewModel(assistedFactory = AppDetailViewModel.Factory::class)
class AppDetailViewModel @AssistedInject constructor(
    private val steamRepository: SteamRepository,
    private val steamChartsRepository: SteamChartsRepository,
    private val isThereAnyDealRepository: IsThereAnyDealRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val priceAlertRepository: PriceAlertRepository,
    private val cacheRepository: CacheRepository,
    @Assisted val key: Route.AppDetail,
) : ViewModel() {
    private val _appData = MutableStateFlow(AppData(key.appId))
    val appData: StateFlow<AppData> = _appData

    private val _appViewState = MutableStateFlow(AppViewState())
    val appViewState: StateFlow<AppViewState> = _appViewState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val cacheResponse = cacheRepository.getCachedData(key.appId)
            cacheResponse.onSuccess { data ->
                Log.d(TAG, "[${key.appId}] Loading data from cache...")
                _appData.update {
                    data.toAppData().copy(
                        isBookmarked = bookmarkRepository.isBookmarked(key.appId),
                        priceAlertInfo = getPriceAlertInfo(),
                    )
                }
                _appViewState.update {
                    AppViewState(
                        steamStatus = if (data.commonDetails == null) NOT_QUEUED else LOADED,
                        isThereAnyDealGameInfoStatus = if (data.isThereAnyDealSlug == null) NOT_QUEUED else LOADED,
                        steamChartsStatus = NOT_QUEUED,
                        isThereAnyDealPriceInfoStatus = if (data.isThereAnyDealPriceInfo == null) NOT_QUEUED else LOADED,
                        dlcsStatus = if (data.dlcs == null) NOT_QUEUED else LOADED,
                    )
                }
            }.onFailure {
                Log.d(TAG, "[${key.appId}] Loading data from internet...")
                fetchDataFromSource(DataType.COMMON_APP_DETAILS)
                _appData.update {
                    it.copy(
                        isBookmarked = bookmarkRepository.isBookmarked(key.appId),
                        priceAlertInfo = getPriceAlertInfo(),
                    )
                }
            }

            _appData
                .drop(1)
                .distinctUntilChanged()
                .distinctUntilChangedBy { it.commonDetails to it.isThereAnyDealId to it.isThereAnyDealSlug to it.isThereAnyDealPriceInfo to it.dlcs }
                .collect { data ->
                    cacheRepository.storeCachedData(data.toSerializableAppData())
                }
        }
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

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _appViewState.update {
                it.copy(
                    refreshStatus = LOADING,
                    selectedTabIndex = 0,
                    steamStatus = NOT_QUEUED,
                    isThereAnyDealGameInfoStatus = NOT_QUEUED,
                    steamChartsStatus = NOT_QUEUED,
                    isThereAnyDealPriceInfoStatus = NOT_QUEUED,
                    dlcsStatus = NOT_QUEUED,
                )
            }

            fetchCommonAppDetails()

            _appViewState.update { it.copy(refreshStatus = LOADED) }
        }
    }

    suspend fun fetchCommonAppDetails() {
        if (appViewState.value.steamStatus != NOT_QUEUED
            && appViewState.value.isThereAnyDealGameInfoStatus != NOT_QUEUED
        ) return

        Log.d(TAG, "[${key.appId}] Fetching common info from steam and ISTAD...")
        _appViewState.update {
            it.copy(steamStatus = LOADING, isThereAnyDealGameInfoStatus = LOADING)
        }
        fetchDataFromSource(DataType.COMMON_APP_DETAILS)

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

        Log.d(TAG, "[${key.appId}] Fetching steam charts per app info...")
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

        Log.d(TAG, "[${key.appId}] Fetching ISTAD Price info...")
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
        if (appData.value.commonDetails?.dlcIds.isNullOrEmpty()) {
            _appViewState.update { it.copy(dlcsStatus = FAILED("No DLCs found!")) }
            return
        }

        Log.d(
            TAG,
            "[${key.appId}] Fetching DLCs info (count: ${appData.value.commonDetails?.dlcIds?.size})..."
        )
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
            Log.d(TAG, "[${key.appId}] Removing bookmark...")
            bookmarkRepository.addBookmark(
                Bookmark(
                    appId = key.appId,
                    name = appData.value.commonDetails!!.name,
                )
            )
            _appData.update { it.copy(isBookmarked = true) }
        } else {
            Log.d(TAG, "[${key.appId}] Adding bookmark...")
            bookmarkRepository.removeBookmark(key.appId)
            _appData.update { it.copy(isBookmarked = false) }
        }
    }

    suspend fun getPriceAlertInfo(): PriceAlert? {
        return priceAlertRepository.getPriceAlert(appId = key.appId)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updatePriceAlert(targetPrice: Float, notifyEveryDay: Boolean) =
        appData.value.commonDetails?.let { commonAppDetails ->
            Log.d(TAG, "[${key.appId}] Adding price alert...")
            priceAlertRepository.addPriceAlert(
                PriceAlert(
                    appId = key.appId,
                    name = commonAppDetails.name,
                    targetPrice = targetPrice,
                    notifyEveryDay = notifyEveryDay,
                    lastFetchedTime = Clock.System.now().toEpochMilliseconds(),
                    lastFetchedPrice = commonAppDetails.currentPrice,
                    originalPrice = commonAppDetails.originalPrice,
                    currency = commonAppDetails.currency,
                )
            )
            _appData.update { it.copy(priceAlertInfo = getPriceAlertInfo()) }
        }

    suspend fun removePriceAlert() {
        Log.d(TAG, "[${key.appId}] Removing price alert...")
        priceAlertRepository.removePriceAlert(appId = key.appId)
        _appData.update { it.copy(priceAlertInfo = getPriceAlertInfo()) }
    }

    fun updateSelectedTabIndex(index: Int) {
        _appViewState.update { it.copy(selectedTabIndex = index) }
    }
}
