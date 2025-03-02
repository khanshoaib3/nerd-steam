package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsScraper
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            SteamChartsScraper().scrape()
        }
    }
}