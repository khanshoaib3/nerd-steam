package com.github.khanshoaib3.nerdsteam.data.repository

import com.github.khanshoaib3.nerdsteam.data.local.detail.PriceAlertDao
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PriceAlert
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

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
        Timber.d("Adding to price alert table: $priceAlert")
        priceAlertDao.insert(priceAlert)
    }

    override suspend fun updatePriceAlert(priceAlert: PriceAlert) {
        Timber.d("Updating tuple in price alert table: $priceAlert")
        priceAlertDao.update(priceAlert)
    }

    override suspend fun getPriceAlert(appId: Int): PriceAlert? {
        if (!priceAlertDao.doesExist(appId))
            return null

        return priceAlertDao.getOne(appId)
    }

    override suspend fun removePriceAlert(appId: Int) {
        Timber.d("Removing tuple from price alert table with appId:$appId")
        priceAlertDao.deleteWithId(appId)
    }

    override fun getAllPriceAlerts(): Flow<List<PriceAlert>> {
        return priceAlertDao.getAll()
    }
}
