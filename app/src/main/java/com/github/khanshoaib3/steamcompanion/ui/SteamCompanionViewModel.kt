package com.github.khanshoaib3.steamcompanion.ui

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import com.github.khanshoaib3.steamcompanion.utils.TopLevelBackStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SteamCompanionViewModel @Inject constructor() : ViewModel() {
    private val _topLevelBackStackState = MutableStateFlow(TopLevelBackStack<Route>(Route.Home))
    val topLevelBackStackState: MutableStateFlow<TopLevelBackStack<Route>> = _topLevelBackStackState
}
