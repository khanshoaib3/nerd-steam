package com.github.khanshoaib3.steamcompanion.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.khanshoaib3.steamcompanion.MainApplication
import com.github.khanshoaib3.steamcompanion.ui.screen.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(mainApplication().container.steamChartsRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MainApplication].
 */
fun CreationExtras.mainApplication(): MainApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
