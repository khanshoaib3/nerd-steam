package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.repository.FeedGamesRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FeedGamesRepository) : ViewModel() {
    init {
        viewModelScope.launch {
            repository.fetchAndStoreData()
        }
    }
}