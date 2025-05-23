package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class BookmarkDataState(
    val bookmarks: List<Bookmark>
)

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    bookmarkRepository: BookmarkRepository
) : ViewModel() {
    val bookmarkDataState: StateFlow<BookmarkDataState> =
        bookmarkRepository.getAllBookmarks()
            .map { bookmarks ->
                BookmarkDataState(bookmarks = bookmarks)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = BookmarkDataState(listOf())
            )
}