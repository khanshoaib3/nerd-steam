package com.github.khanshoaib3.steamcompanion.data.repository

import com.github.khanshoaib3.steamcompanion.data.remote.IsThereAnyDealApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit

class IsThereAnyDealRepositoryTest {

    @Before
    fun setup() {
    }

    val isThereAnyDealRepository = OnlineIsThereAnyDealRepository(
        isThereAnyDealApiService = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://api.isthereanydeal.com")
            .build()
            .create(IsThereAnyDealApiService::class.java)
    )

    @Test
    fun testGameLookup_failure() = runTest {
        val response = isThereAnyDealRepository.lookupGame(69)
        Assert.assertTrue(response.isFailure)
        Assert.assertEquals(
            "Game with appId 69 not found in IsThereAnyDeal.com",
            response.exceptionOrNull()?.message
        )
    }

    @Test
    fun testGameLookup_success_half_life_2() = runTest {
        val response = isThereAnyDealRepository.lookupGame(220)
        Assert.assertTrue(response.isSuccess)
        response.onSuccess {
            Assert.assertEquals("half-life-2", it.slug)
            print(it)
        }
    }

    @Test
    fun testGameInfo_uid_halfLife() = runTest {
        val response = isThereAnyDealRepository.getGameInfo("018d937f-012f-73b8-ab2c-898516969e6a")
        response.onSuccess {
            Assert.assertEquals(it.appid, 220)
            Assert.assertEquals("half-life-2", it.slug)
            print(it)
        }
    }

    @Test
    fun testGameInfo_appId_halfLife() = runTest {
        val response = isThereAnyDealRepository.getGameInfoFromAppId(220)
        response.onSuccess {
            Assert.assertEquals(it.appid, 220)
            Assert.assertEquals("half-life-2", it.slug)
            print(it)
        }
    }

    @Test
    fun testPriceInfo_success() = runTest {
        val response = isThereAnyDealRepository.getPriceInfo(
            listOf(
                "018d937f-012f-73b8-ab2c-898516969e6a", // Half life 2
                "018d937f-679e-7276-b6f1-e59da609dd4b", // the last of us
                "018d937f-7b20-72f6-a2de-dc2ff64bae95", // the last of us ii remastered
            )
        )
        response.onSuccess {
            Assert.assertEquals(3, it.size)
            println(it)
        }
    }

    @Test
    fun testPriceInfo_failure() = runTest {
        val response = isThereAnyDealRepository.getPriceInfo("wrong-id")
        Assert.assertTrue(response.isFailure)
        Assert.assertEquals("HTTP 400", response.exceptionOrNull()?.message?.trim())
//        Assert.assertEquals("HTTP 400", (response.exceptionOrNull() as HttpException).response()?.errorBody()?.string())
    }
}