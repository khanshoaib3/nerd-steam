package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import com.github.khanshoaib3.nerdsteam.ui.components.ErrorColumn
import com.github.khanshoaib3.nerdsteam.ui.components.TwoPaneScene
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components.AppDetailsCard
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components.PriceAlertSheet
import com.github.khanshoaib3.nerdsteam.utils.Progress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppDetailsScreenRoot(
    isWideScreen: Boolean,
    isInTwoPaneScene: Boolean,
    onUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppDetailViewModel = hiltViewModel(),
) {
    val appData by viewModel.appData.collectAsState()
    val viewState by viewModel.appViewState.collectAsState()
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val context = LocalContext.current

    val setPriceAlert: (Float, Boolean) -> Unit = { targetPrice, notifyEveryDay ->
        val toast = Toast.makeText(
            context,
            "Alert set for ${appData.commonDetails?.name ?: "the app"}",
            Toast.LENGTH_SHORT
        )
        scope.launch(context = Dispatchers.IO) {
            viewModel.updatePriceAlert(targetPrice, notifyEveryDay)
            toast.show()
        }
    }
    val removePriceAlert: () -> Unit = {
        val toast = Toast.makeText(
            context,
            "Alert removed",
            Toast.LENGTH_SHORT
        )
        scope.launch(context = Dispatchers.IO) {
            viewModel.removePriceAlert()
            toast.show()
        }
    }


    if (viewState.isThereAnyDealGameInfoStatus != Progress.LOADED && viewState.steamStatus != Progress.LOADED) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewState.isThereAnyDealGameInfoStatus is Progress.FAILED && viewState.steamStatus is Progress.FAILED) {
                ErrorColumn(reason = (viewState.steamStatus as Progress.FAILED).reason)
            } else {
                CircularWavyProgressIndicator(Modifier.scale(2.5f))
            }
        }
        return
    }

    val toggleBookmarkCallback: () -> Unit = {
        scope.launch(context = Dispatchers.IO) {
            viewModel.toggleBookmarkStatus()
        }
        view.performHapticFeedback(HapticFeedbackConstantsCompat.KEYBOARD_TAP)
    }

    val onRefreshCallback: () -> Unit = {
        viewModel.refresh()
        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
    }

    AppDetailsScreen(
        appData = appData,
        appViewState = viewState,
        fetchDataFromSourceCallback = viewModel::fetchDataFromSource,
        updateSelectedTabIndexCallback = viewModel::updateSelectedTabIndex,
        onBookmarkClick = toggleBookmarkCallback,
        isBookmarkActive = appData.isBookmarked,
        storedPriceAlertInfo = appData.priceAlertInfo,
        setPriceAlert = setPriceAlert,
        removePriceAlert = removePriceAlert,
        isInTwoPaneScene = isInTwoPaneScene,
        onRefreshCallback = onRefreshCallback,
        onUpButtonClick = onUpButtonClick,
        modifier = modifier,
        isWideScreen = isWideScreen,
        topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppDetailsScreen(
    appData: AppData,
    appViewState: AppViewState,
    fetchDataFromSourceCallback: (DataType) -> Unit,
    updateSelectedTabIndexCallback: (Int) -> Unit,
    onBookmarkClick: () -> Unit,
    isBookmarkActive: Boolean,
    storedPriceAlertInfo: PriceAlert?,
    setPriceAlert: (Float, Boolean) -> Unit,
    removePriceAlert: () -> Unit,
    isInTwoPaneScene: Boolean,
    onRefreshCallback: () -> Unit,
    onUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isWideScreen: Boolean = false,
    refreshState: PullToRefreshState = rememberPullToRefreshState(),
    topAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {
    val sheetState = rememberModalBottomSheetState()
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    val notificationOptions = listOf("Everyday", "Once")
    var selectedNotificationOptionIndex by remember { mutableIntStateOf(0) }

    val maxPrice = appData.commonDetails!!.originalPrice
    val currentPrice = appData.commonDetails.currentPrice
    var targetPrice by remember(appData.steamAppId) { mutableFloatStateOf(currentPrice) }
    LaunchedEffect(storedPriceAlertInfo) {
        if (storedPriceAlertInfo == null) return@LaunchedEffect
        targetPrice = storedPriceAlertInfo.targetPrice
        selectedNotificationOptionIndex = if (storedPriceAlertInfo.notifyEveryDay) 0 else 1
    }
    var showPriceAlertSheet by rememberSaveable { mutableStateOf(false) }
    val hideSheetCallback: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showPriceAlertSheet = false
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = appViewState.refreshStatus == Progress.LOADING,
        onRefresh = onRefreshCallback,
        state = refreshState,
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = appViewState.refreshStatus == Progress.LOADING,
                state = refreshState,
            )
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isInTwoPaneScene && TwoPaneScene.IsActive) {
            AppDetailsCard(
                appData = appData,
                appViewState = appViewState,
                fetchDataFromSourceCallback = fetchDataFromSourceCallback,
                onBookmarkClick = onBookmarkClick,
                isBookmarkActive = isBookmarkActive,
                storedPriceAlertInfo = storedPriceAlertInfo,
                showPriceAlertSheetCallback = removePriceAlert,
                updateSelectedTabIndexCallback = updateSelectedTabIndexCallback,
                showHeader = true,
            )
            return@PullToRefreshBox
        }

        Scaffold(
            modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(appData.commonDetails.name) },
                    navigationIcon = {
                        IconButton(onClick = onUpButtonClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, "Go back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onBookmarkClick) {
                            Icon(
                                if (isBookmarkActive) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark app"
                            )
                        }
                    },
                    scrollBehavior = topAppBarScrollBehavior,
                    windowInsets = WindowInsets()
                )
            }) { innerPadding ->
            AppDetailsCard(
                appData = appData,
                appViewState = appViewState,
                fetchDataFromSourceCallback = fetchDataFromSourceCallback,
                onBookmarkClick = onBookmarkClick,
                isBookmarkActive = isBookmarkActive,
                storedPriceAlertInfo = appData.priceAlertInfo,
                showPriceAlertSheetCallback = {showPriceAlertSheet = true},
                updateSelectedTabIndexCallback = updateSelectedTabIndexCallback,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                showHeader = false,
                isWideScreen = isWideScreen,
            )
        }

        if (showPriceAlertSheet) {
            @Suppress("AssignedValueIsNeverRead")
            PriceAlertSheet(
                sheetState = sheetState,
                targetPrice = targetPrice,
                maxPrice = maxPrice,
                onPriceChange = { targetPrice = (it * 100).roundToInt() / 100f },
                selectedNotificationOptionIndex = selectedNotificationOptionIndex,
                notificationOptions = notificationOptions,
                onSelectedNotificationOptionIndexChange = { selectedNotificationOptionIndex = it },
                onConfirm = {
                    setPriceAlert(targetPrice, selectedNotificationOptionIndex == 0)
                    view.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                    hideSheetCallback()
                },
                alertAlreadySet = storedPriceAlertInfo != null,
                onStop = {
                    removePriceAlert()
                    view.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                    hideSheetCallback()
                },
                onCancel = { hideSheetCallback() },
                onDismiss = { showPriceAlertSheet = false }
            )
        }
    }
}
