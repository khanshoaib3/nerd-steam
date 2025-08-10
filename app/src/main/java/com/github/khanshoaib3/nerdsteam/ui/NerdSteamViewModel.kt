package com.github.khanshoaib3.nerdsteam.ui

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.nerdsteam.ui.utils.Route
import com.github.khanshoaib3.nerdsteam.ui.utils.TopLevelRoute
import com.github.khanshoaib3.nerdsteam.utils.TopLevelBackStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NerdSteamViewModel @Inject constructor() : ViewModel() {
    private val _topLevelBackStackState = MutableStateFlow(TopLevelBackStack<Route>(TopLevelRoute.Home))
    val topLevelBackStackState: MutableStateFlow<TopLevelBackStack<Route>> = _topLevelBackStackState
}
