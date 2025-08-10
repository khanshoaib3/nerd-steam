package com.github.khanshoaib3.nerdsteam.di

import com.github.khanshoaib3.nerdsteam.data.repository.BookmarkRepository
import com.github.khanshoaib3.nerdsteam.data.repository.IsThereAnyDealRepository
import com.github.khanshoaib3.nerdsteam.data.repository.LocalBookmarkRepository
import com.github.khanshoaib3.nerdsteam.data.repository.LocalPriceAlertRepository
import com.github.khanshoaib3.nerdsteam.data.repository.OnlineIsThereAnyDealRepository
import com.github.khanshoaib3.nerdsteam.data.repository.OnlineSteamRepository
import com.github.khanshoaib3.nerdsteam.data.repository.PriceAlertRepository
import com.github.khanshoaib3.nerdsteam.data.repository.ScraperSteamChartsRepository
import com.github.khanshoaib3.nerdsteam.data.repository.SteamChartsRepository
import com.github.khanshoaib3.nerdsteam.data.repository.SteamRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// TODO Optimise lifetime https://developer.android.com/training/dependency-injection/hilt-android#component-lifetimes
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSteamChartsRepository(
        steamChartsRepository: ScraperSteamChartsRepository,
    ): SteamChartsRepository

    @Binds
    @Singleton
    abstract fun bindAppDetailRepository(
        appDetailRepository: OnlineSteamRepository
    ): SteamRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        bookmarkRepository: LocalBookmarkRepository
    ): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindIsThereAnyDealRepository(
        isThereAnyDealRepository: OnlineIsThereAnyDealRepository
    ): IsThereAnyDealRepository

    @Binds
    @Singleton
    abstract fun bindPriceAlertRepository(
        priceAlertRepository: LocalPriceAlertRepository
    ): PriceAlertRepository
}