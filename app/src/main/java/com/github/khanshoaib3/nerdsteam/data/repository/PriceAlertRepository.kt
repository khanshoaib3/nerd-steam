package com.github.khanshoaib3.nerdsteam.data.repository

import android.util.Log
import com.github.khanshoaib3.nerdsteam.data.local.detail.PriceAlertDao
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "PriceAlertRepository"

interface PriceAlertRepository {

    suspend fun addPriceAlert(priceAlert: PriceAlert)

    suspend fun updatePriceAlert(priceAlert: PriceAlert)

    suspend fun removePriceAlert(appId: Int)

    suspend fun getPriceAlert(appId: Int): PriceAlert?

    fun getAllPriceAlerts(): Flow<List<PriceAlert>>
}

class LocalPriceAlertRepository @Inject constructor(
    private val priceAlertDao: PriceAlertDao,
) : PriceAlertRepository {

    override suspend fun addPriceAlert(priceAlert: PriceAlert) {
        Log.d(TAG, "Adding to price alert table: $priceAlert")
        priceAlertDao.insert(priceAlert)
    }

    override suspend fun updatePriceAlert(priceAlert: PriceAlert) {
        Log.d(TAG, "Updating tuple in price alert table: $priceAlert")
        priceAlertDao.update(priceAlert)
    }

    override suspend fun getPriceAlert(appId: Int): PriceAlert? {
        if (!priceAlertDao.doesExist(appId))
            return null

        return priceAlertDao.getOne(appId)
    }

    override suspend fun removePriceAlert(appId: Int) {
        Log.d(TAG, "Removing tuple from price alert table with appId:$appId")
        priceAlertDao.deleteWithId(appId)
    }

    override fun getAllPriceAlerts(): Flow<List<PriceAlert>> {
        return priceAlertDao.getAll()
    }
}
