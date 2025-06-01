package com.github.khanshoaib3.steamcompanion.ui.screen.detail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.model.detail.PriceTracking
import com.github.khanshoaib3.steamcompanion.data.model.detail.SteamWebApiAppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.repository.BookmarkRepository
import com.github.khanshoaib3.steamcompanion.data.repository.GameDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class GameData(
    val content: SteamWebApiAppDetailsResponse? = null,
    val isBookmarked: Boolean = false
)

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gameDetailRepository: GameDetailRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    private val _gameData = MutableStateFlow(GameData())
    val gameData: StateFlow<GameData> = _gameData

    suspend fun updateAppId(appId: Int?) {
        if (appId == null) return
        if (appId == 0) return

        _gameData.update {
            val content = gameDetailRepository.fetchDataForAppId(appId = appId)
            val isBookmarked = bookmarkRepository.isBookmarked(content?.data?.steamAppId)
            GameData(
                content = content,
                isBookmarked = isBookmarked
            )
        }
    }

    suspend fun toggleBookmarkStatus() {
        gameData.value.apply {
            if (content?.data?.steamAppId == null) return

            if (!isBookmarked) {
                bookmarkRepository.addBookmark(
                    Bookmark(
                        appId = content.data.steamAppId,
                        name = content.data.name,
                    )
                )
                _gameData.update {
                    it.copy(
                        isBookmarked = true
                    )
                }
            } else {
                bookmarkRepository.removeBookmark(content.data.steamAppId)
                _gameData.update {
                    it.copy(
                        isBookmarked = false
                    )
                }
            }
        }
    }

    suspend fun getPriceTrackingInfo() : PriceTracking? {
        if (gameData.value.content?.data?.steamAppId == null) return null

        return gameDetailRepository.getPriceTrackingInfo(appId = gameData.value.content!!.data!!.steamAppId)
    }

    suspend fun startPriceTracking(targetPrice: Float, notifyEveryDay: Boolean) {
        if (gameData.value.content?.data?.steamAppId == null) return

        gameDetailRepository.trackPrice(
            PriceTracking(
                appId = gameData.value.content!!.data!!.steamAppId,
                gameName = gameData.value.content!!.data!!.name,
                targetPrice = targetPrice,
                notifyEveryDay = notifyEveryDay,
            )
        )
    }

    suspend fun stopPriceTracking() {
        if (gameData.value.content?.data?.steamAppId == null) return

        gameDetailRepository.stopTracking(appId = gameData.value.content!!.data!!.steamAppId)
    }
}
