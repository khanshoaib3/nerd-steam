package com.github.khanshoaib3.steamcompanion.di

import com.github.khanshoaib3.steamcompanion.data.repository.ScraperSteamChartsRepository
import com.github.khanshoaib3.steamcompanion.data.repository.SteamChartsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSteamChartsRepository(
        steamChartsRepository: ScraperSteamChartsRepository,
    ): SteamChartsRepository
}