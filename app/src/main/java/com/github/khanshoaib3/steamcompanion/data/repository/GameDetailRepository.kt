package com.github.khanshoaib3.steamcompanion.data.repository

interface GameDetailRepository {
    suspend fun fetchDataForAppId(appId: Int)
}

class OnlineGameDetailRepository() : GameDetailRepository {
    override suspend fun fetchDataForAppId(appId: Int) {

    }
}