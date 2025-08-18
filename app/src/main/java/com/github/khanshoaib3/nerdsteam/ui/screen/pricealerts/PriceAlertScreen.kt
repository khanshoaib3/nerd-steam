package com.github.khanshoaib3.nerdsteam.ui.screen.pricealerts

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.khanshoaib3.nerdsteam.ui.navigation.components.CommonTopAppBar
import com.github.khanshoaib3.nerdsteam.ui.screen.pricealerts.components.PriceAlertTable
import com.github.khanshoaib3.nerdsteam.ui.utils.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceAlertScreenRoot(
    navigateBackCallback: () -> Unit,
    addAppDetailPane: (Int) -> Unit,
    modifier: Modifier = Modifier,
    priceAlertViewModel: PriceAlertViewModel = hiltViewModel(),
) {
    val localView = LocalView.current
    val sortedAlerts by priceAlertViewModel.sortedAlerts.collectAsState()

    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val onGameHeaderClick: () -> Unit = {
        priceAlertViewModel.toggleSortOrderOfTypeName()
        localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }
    val onPriceHeaderClick: () -> Unit = {
        priceAlertViewModel.toggleSortOrderOfTypePrice()
        localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonTopAppBar(
                scrollBehavior = scrollBehavior,
                showMenuButton = false,
                onMenuButtonClick = {},
                navigateBackCallback = navigateBackCallback,
                forRoute = Route.Alerts,
                windowInsets = WindowInsets(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) { innerPadding ->
        PriceAlertScreen(
            alerts = sortedAlerts,
            onGameClick = addAppDetailPane,
            onNameHeaderClick = onGameHeaderClick,
            onPriceHeaderClick = onPriceHeaderClick,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        )
    }
}

@Composable
fun PriceAlertScreen(
    alerts: List<PriceAlertDisplay>,
    onGameClick: (Int) -> Unit,
    onNameHeaderClick: () -> Unit,
    onPriceHeaderClick: () -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    PriceAlertTable(
        alerts,
        onGameClick,
        onNameHeaderClick,
        onPriceHeaderClick,
        imageWidth,
        imageHeight,
        modifier
    )
}
