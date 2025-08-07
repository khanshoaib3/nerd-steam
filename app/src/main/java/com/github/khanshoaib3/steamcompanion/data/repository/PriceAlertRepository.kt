package com.github.khanshoaib3.steamcompanion.data.repository

import com.github.khanshoaib3.steamcompanion.data.local.detail.PriceAlertDao
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.PriceAlert
import com.github.khanshoaib3.steamcompanion.data.remote.SteamInternalWebApiService
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
        priceAlertDao.insert(priceAlert)
    }

    override suspend fun updatePriceAlert(priceAlert: PriceAlert) {
        priceAlertDao.update(priceAlert)
    }

    override suspend fun getPriceAlert(appId: Int): PriceAlert? {
        if (!priceAlertDao.doesExist(appId))
            return null

        return priceAlertDao.getOne(appId)
    }

    override suspend fun removePriceAlert(appId: Int) {
        priceAlertDao.deleteWithId(appId)
    }

    override fun getAllPriceAlerts(): Flow<List<PriceAlert>> {
        return priceAlertDao.getAll()
    }
}
