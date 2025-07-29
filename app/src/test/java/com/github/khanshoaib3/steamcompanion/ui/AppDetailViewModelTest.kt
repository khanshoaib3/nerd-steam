package com.github.khanshoaib3.steamcompanion.ui

import android.content.Context
import com.github.khanshoaib3.steamcompanion.data.repository.LocalBookmarkRepository
import com.github.khanshoaib3.steamcompanion.data.repository.OnlineAppDetailRepository
import com.github.khanshoaib3.steamcompanion.data.repository.OnlineIsThereAnyDealRepository
import com.github.khanshoaib3.steamcompanion.di.AppModule
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppDetailViewModel
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class AppDetailViewModelTest {
    private lateinit var context: Context
    private val appModule = AppModule()
    private lateinit var viewmodel: AppDetailViewModel

    @Before
    fun initialize() {
        context = mock<Context> { }

        viewmodel = AppDetailViewModel(
            appDetailRepository = OnlineAppDetailRepository(
                steamInternalWebApiService = appModule.provideSteamInternalWebApiService(),
                priceTrackingDao = appModule.providePriceTrackingDao(context)
            ),
            isThereAnyDealRepository = OnlineIsThereAnyDealRepository(appModule.provideIsThereAnyDealApiService()),
            bookmarkRepository = LocalBookmarkRepository(appModule.provideBookmarkDao(context)),
            key = Route.AppDetail(appId = 220)
        )

    }

    @Test
    fun `Test fetching of price info from IsThereAnyDeal`() {
        runBlocking {
            viewmodel.fetchISTDPriceInfo()
            viewmodel.collatedAppData.value.let {
                println(it.isThereAnyDealPriceInfo)
            }
        }
    }
}