package com.github.khanshoaib3.steamcompanion.data.repository

import javax.inject.Inject

interface GameDetailRepository {
    suspend fun fetchDataForAppId(appId: Int)
}

class OnlineGameDetailRepository @Inject constructor() : GameDetailRepository {
    override suspend fun fetchDataForAppId(appId: Int) {

    }
}