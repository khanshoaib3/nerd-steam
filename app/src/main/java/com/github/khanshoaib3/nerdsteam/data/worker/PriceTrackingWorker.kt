package com.github.khanshoaib3.nerdsteam.data.worker

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.data.local.MainDatabase
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import com.github.khanshoaib3.nerdsteam.data.remote.SteamInternalWebApiService
import com.github.khanshoaib3.nerdsteam.data.repository.LocalPriceAlertRepository
import com.github.khanshoaib3.nerdsteam.data.repository.OnlineSteamRepository
import com.github.khanshoaib3.nerdsteam.di.AppModule
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class PriceAlertsWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    private val db = MainDatabase.getDatabase(appContext)
    val steamInternalWebApiService: SteamInternalWebApiService =
        AppModule().provideSteamInternalWebApiService()

    private val steamRepository =
        OnlineSteamRepository(
            steamInternalWebApiService = steamInternalWebApiService,
            steamCommunityApiService = AppModule().provideSteamCommunityService()
        )
    private val priceAlertRepository = LocalPriceAlertRepository(priceAlertDao = db.priceAlertDao())

    @OptIn(ExperimentalTime::class)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            priceAlertRepository.getAllPriceAlerts().collect {
                it.onEach { entry ->
                    val currentPrice = fetchCurrentPrice(entry.appId)
                    if (currentPrice == null) return@onEach

                    if (currentPrice <= entry.targetPrice) {
                        showNotification(entry, currentPrice)
                        if (!entry.notifyEveryDay) {
                            priceAlertRepository.removePriceAlert(entry.appId)
                            return@onEach
                        }
                    }

                    priceAlertRepository.updatePriceAlert(
                        entry.copy(
                            lastFetchedPrice = currentPrice,
                            lastFetchedTime = Clock.System.now().toEpochMilliseconds()
                        )
                    )
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun fetchCurrentPrice(appId: Int): Float? {
        steamRepository.fetchDataForAppId(appId)
            .onSuccess { return it.priceOverview?.finalPrice?.div(100f) }
        return null
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(entry: PriceAlert, currentPrice: Float) {
        try {
            val notificationManager = NotificationManagerCompat.from(appContext)
            val notification = NotificationCompat.Builder(appContext, "price_alert_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Price Drop: ${entry.name}")
                .setContentText("Now only ₹$currentPrice!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(entry.appId, notification)
        } catch (e: Exception) {
            Log.e("PriceAlertWorker", "Failed to show notification", e)
        }
    }
}
