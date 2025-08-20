package com.github.khanshoaib3.nerdsteam.data.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.khanshoaib3.nerdsteam.PRICE_ALERT_CHANNEL_ID
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.data.local.MainDatabase
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import com.github.khanshoaib3.nerdsteam.data.remote.SteamInternalWebApiService
import com.github.khanshoaib3.nerdsteam.data.repository.LocalPriceAlertRepository
import com.github.khanshoaib3.nerdsteam.data.repository.OnlineSteamRepository
import com.github.khanshoaib3.nerdsteam.di.AppModule
import com.github.khanshoaib3.nerdsteam.utils.getNumberFormatFromCurrencyCode
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
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
            priceAlertRepository.getAllPriceAlerts().firstOrNull()?.forEach { entry ->
                val currentPrice = fetchCurrentPrice(entry.appId)
                if (currentPrice == null) return@forEach

                if (currentPrice <= entry.targetPrice) {
                    showNotification(entry, currentPrice, entry.currency)
                    if (!entry.notifyEveryDay) {
                        priceAlertRepository.removePriceAlert(entry.appId)
                        return@forEach
                    }
                }

                priceAlertRepository.updatePriceAlert(
                    entry.copy(
                        lastFetchedPrice = currentPrice,
                        lastFetchedTime = Clock.System.now().toEpochMilliseconds()
                    )
                )
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun fetchCurrentPrice(appId: Int): Float? {
        val response = steamRepository.fetchDataForAppId(appId)
        return response.getOrNull()?.priceOverview?.finalPrice?.div(100f)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(entry: PriceAlert, currentPrice: Float, currencyCode: String) {
        try {
            val currencyFormatter = getNumberFormatFromCurrencyCode(currencyCode)
            val notificationManager = NotificationManagerCompat.from(appContext)
            val notification = NotificationCompat.Builder(appContext, PRICE_ALERT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Price Drop: ${entry.name}")
                .setContentText("Now only ${currencyFormatter.format(currentPrice)}!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(entry.appId, notification)
        } catch (e: Exception) {
            Timber.e("Failed to show notification")
            Timber.e(e)
        }
    }
}
