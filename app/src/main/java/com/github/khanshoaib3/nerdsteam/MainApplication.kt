package com.github.khanshoaib3.nerdsteam

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.khanshoaib3.nerdsteam.data.worker.PriceAlertsWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

internal const val PRICE_ALERT_CHANNEL_ID = "price_alerts_channel"

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)

        val workRequest = PeriodicWorkRequestBuilder<PriceAlertsWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "price_alert_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    /* For testing
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)

        val workRequest = OneTimeWorkRequestBuilder<PriceAlertsWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS) // short delay for testing
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
    */

    private fun createNotificationChannel(context: Context) {
        val channelId = PRICE_ALERT_CHANNEL_ID
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

