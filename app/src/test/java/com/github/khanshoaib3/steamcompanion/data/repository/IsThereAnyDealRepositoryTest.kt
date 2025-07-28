package com.github.khanshoaib3.steamcompanion.data.repository

import com.github.khanshoaib3.steamcompanion.data.remote.IsThereAnyDealApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit

class IsThereAnyDealRepositoryTest {
    val isThereAnyDealRepository = OnlineIsThereAnyDealRepository(
        isThereAnyDealApiService = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://api.isthereanydeal.com")
            .build()
            .create(IsThereAnyDealApiService::class.java)
    )

    @Test
    fun testGameLookup_found() = runTest {
        val response = isThereAnyDealRepository.lookupGame(220)
        Assert.assertTrue(response.found)
        Assert.assertEquals("half-life-2", response.gameInfoShort?.slug)
        print(response)
    }

    @Test
    fun testGameInfo_halfLife() = runTest {
        val response = isThereAnyDealRepository.getGameInfo("018d937f-012f-73b8-ab2c-898516969e6a")
        Assert.assertEquals(response.appid, 220)
        Assert.assertEquals("half-life-2", response.slug)
        print(response)
    }

    @Test
    fun testPriceInfo() = runTest {
        val response = isThereAnyDealRepository.getPriceInfo(
            listOf(
                "018d937f-012f-73b8-ab2c-898516969e6a", // Half life 2
                "018d937f-679e-7276-b6f1-e59da609dd4b", // the last of us
                "018d937f-7b20-72f6-a2de-dc2ff64bae95", // the last of us ii remastered
            )
        )
        Assert.assertEquals(3, response.size)
        println(response)
    }
}