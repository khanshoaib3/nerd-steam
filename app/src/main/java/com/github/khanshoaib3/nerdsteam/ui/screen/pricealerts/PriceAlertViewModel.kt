package com.github.khanshoaib3.nerdsteam.ui.screen.pricealerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import com.github.khanshoaib3.nerdsteam.data.repository.PriceAlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class PriceAlertTableSortOrder {
    Default, NameAsc, NameDesc, PriceAsc, PriceDesc
}

data class PriceAlertDisplay(
    val name: String,
    val appId: Int,
    val imageUrl: String,
    val currentPrice: Float,
    val currency: String,
)

fun PriceAlert.toDisplayModel() = PriceAlertDisplay(
    name = this.name,
    appId = this.appId,
    imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/${this.appId}/library_600x900.jpg",
    currentPrice = this.lastFetchedPrice,
    currency = this.currency,
)

data class PriceAlertDataState(
    val alerts: List<PriceAlert>,
)

data class PriceAlertViewState(
    val tableSortOrder: PriceAlertTableSortOrder = PriceAlertTableSortOrder.Default,
)

@HiltViewModel
class PriceAlertViewModel @Inject constructor(
    priceAlertRepository: PriceAlertRepository,
) : ViewModel() {
    val priceAlertDataState: StateFlow<PriceAlertDataState> =
        priceAlertRepository.getAllPriceAlerts()
            .map {
                PriceAlertDataState(alerts = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = PriceAlertDataState(listOf())
            )

    private val _priceAlertViewState = MutableStateFlow(PriceAlertViewState())
    val priceAlertViewState: StateFlow<PriceAlertViewState> = _priceAlertViewState

    val sortedAlerts: StateFlow<List<PriceAlertDisplay>> = combine(
        priceAlertDataState,
        priceAlertViewState
    ) { dataState, viewState ->
        when (viewState.tableSortOrder) {
            PriceAlertTableSortOrder.PriceAsc -> dataState.alerts
                .sortedBy { it.lastFetchedPrice }
                .map { it.toDisplayModel() }

            PriceAlertTableSortOrder.PriceDesc -> dataState.alerts
                .sortedByDescending { it.lastFetchedPrice }
                .map { it.toDisplayModel() }

            PriceAlertTableSortOrder.NameAsc -> dataState.alerts
                .sortedBy { it.name.lowercase() }
                .map { it.toDisplayModel() }

            PriceAlertTableSortOrder.NameDesc -> dataState.alerts
                .sortedByDescending { it.name.lowercase() }
                .map { it.toDisplayModel() }

            PriceAlertTableSortOrder.Default -> dataState.alerts
                .map { it.toDisplayModel() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    private fun updateSortOrder(newSortOrder: PriceAlertTableSortOrder) {
        _priceAlertViewState.update { it.copy(tableSortOrder = newSortOrder) }
    }

    fun toggleSortOrderOfTypePrice() = updateSortOrder(
        newSortOrder = when (priceAlertViewState.value.tableSortOrder) {
            PriceAlertTableSortOrder.PriceAsc -> PriceAlertTableSortOrder.PriceDesc
            else -> PriceAlertTableSortOrder.PriceAsc
        }
    )

    fun toggleSortOrderOfTypeName() = updateSortOrder(
        newSortOrder = when (priceAlertViewState.value.tableSortOrder) {
            PriceAlertTableSortOrder.NameAsc -> PriceAlertTableSortOrder.NameDesc
            else -> PriceAlertTableSortOrder.NameAsc
        }
    )
}
