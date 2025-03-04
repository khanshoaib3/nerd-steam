package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.local.LocalDataStoreRepository
import com.github.khanshoaib3.steamcompanion.data.repository.SteamChartsRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val steamChartsRepository: SteamChartsRepository,
    private val localDataStoreRepository: LocalDataStoreRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            steamChartsRepository.fetchAndStoreData()
        }
    }
}