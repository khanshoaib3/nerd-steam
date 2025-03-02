package com.github.khanshoaib3.steamcompanion

import android.app.Application
import com.github.khanshoaib3.steamcompanion.di.AppContainer
import com.github.khanshoaib3.steamcompanion.di.AppDataContainer

class MainApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}