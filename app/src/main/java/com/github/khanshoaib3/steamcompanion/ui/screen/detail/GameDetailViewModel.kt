package com.github.khanshoaib3.steamcompanion.ui.screen.detail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.data.model.detail.SteamWebApiAppDetailsResponse
import com.github.khanshoaib3.steamcompanion.data.repository.GameDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class GameData(
    val content: SteamWebApiAppDetailsResponse? = null,
)

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gameDetailRepository: GameDetailRepository,
) : ViewModel() {
    private val _gameData = MutableStateFlow<GameData>(GameData())
    val gameData: StateFlow<GameData> = _gameData

    suspend fun updateAppId(appId: Int?) {
        if (appId == null) return
        if (appId == 0) return

        _gameData.update {
            GameData(
                content = gameDetailRepository.fetchDataForAppId(
                    appId = appId
                )
            )
        }
    }
}
