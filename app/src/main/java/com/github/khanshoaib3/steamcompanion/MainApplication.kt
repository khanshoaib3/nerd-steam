package com.github.khanshoaib3.steamcompanion

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.khanshoaib3.steamcompanion.data.worker.PriceCheckWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)

        val workRequest = PeriodicWorkRequestBuilder<PriceCheckWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "price_check_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    /*
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)

        val workRequest = OneTimeWorkRequestBuilder<PriceCheckWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS) // short delay for testing
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
    */


    private fun createNotificationChannel(context: Context) {
        val channelId = "price_channel"
        val name = "Price Drop Alerts"
        val descriptionText = "Notifies when a game's price drops below your target"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

