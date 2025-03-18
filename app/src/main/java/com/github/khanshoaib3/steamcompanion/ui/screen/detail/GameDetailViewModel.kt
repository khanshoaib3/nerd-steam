package com.github.khanshoaib3.steamcompanion.ui.screen.detail
// Ref(assisted inject): https://medium.com/@cgaisl/how-to-pass-arguments-to-a-hiltviewmodel-from-compose-97c74a75f772

import androidx.lifecycle.ViewModel
import com.github.khanshoaib3.steamcompanion.data.repository.GameDetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = GameDetailViewModel.GameDetailViewModelFactory::class)
class GameDetailViewModel @AssistedInject constructor(
    @Assisted private val appId: Int?,
    private val gameDetailRepository: GameDetailRepository,
) : ViewModel() {
    @AssistedFactory
    interface GameDetailViewModelFactory {
        fun create(id: Int?): GameDetailViewModel
    }

    fun getText(): String {
        return "Selected game with id: ${appId ?: 0}"
    }
}
