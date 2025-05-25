package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.formattedTimestamp
import com.github.khanshoaib3.steamcompanion.data.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class BookmarkTableSortOrder {
    TimeAsc,
    TimeDesc,
    NameAsc,
    NameDesc
}

data class BookmarkDataState(
    val bookmarks: List<Bookmark>
)

data class BookmarkViewState(
    val tableSortOrder: BookmarkTableSortOrder = BookmarkTableSortOrder.TimeAsc
)

data class BookmarkDisplay(
    val appId: String,
    val name: String,
    val formattedTime: String
)

fun Bookmark.toDisplayModel(): BookmarkDisplay {
    return BookmarkDisplay(
        appId = this.appId.toString(),
        name = this.name,
        formattedTime = formattedTimestamp()
    )
}


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

    private val _bookmarkViewState = MutableStateFlow(BookmarkViewState())
    val bookmarkViewState: StateFlow<BookmarkViewState> = _bookmarkViewState

    val sortedBookmarks: StateFlow<List<BookmarkDisplay>> = combine(
        bookmarkDataState,
        bookmarkViewState
    ) { dataState, viewState ->
        when (viewState.tableSortOrder) {
            BookmarkTableSortOrder.TimeAsc -> dataState.bookmarks
                .sortedBy { it.timeStamp }
                .map { it.toDisplayModel() }

            BookmarkTableSortOrder.TimeDesc -> dataState.bookmarks
                .sortedByDescending { it.timeStamp }
                .map { it.toDisplayModel() }

            BookmarkTableSortOrder.NameAsc -> dataState.bookmarks
                .sortedBy { it.name.lowercase() }
                .map { it.toDisplayModel() }

            BookmarkTableSortOrder.NameDesc -> dataState.bookmarks
                .sortedByDescending { it.name.lowercase() }
                .map { it.toDisplayModel() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    private fun updateSortOrder(newSortOrder: BookmarkTableSortOrder) {
        _bookmarkViewState.update { it.copy(tableSortOrder = newSortOrder) }
    }

    fun toggleSortOrderOfTypeTime() = updateSortOrder(
        newSortOrder = when (bookmarkViewState.value.tableSortOrder) {
            BookmarkTableSortOrder.NameAsc, BookmarkTableSortOrder.NameDesc -> BookmarkTableSortOrder.TimeAsc
            BookmarkTableSortOrder.TimeAsc -> BookmarkTableSortOrder.TimeDesc
            else -> BookmarkTableSortOrder.TimeAsc
        }
    )

    fun toggleSortOrderOfTypeName() = updateSortOrder(
        newSortOrder = when (bookmarkViewState.value.tableSortOrder) {
            BookmarkTableSortOrder.TimeAsc, BookmarkTableSortOrder.TimeDesc -> BookmarkTableSortOrder.NameAsc
            BookmarkTableSortOrder.NameAsc -> BookmarkTableSortOrder.NameDesc
            else -> BookmarkTableSortOrder.NameAsc
        }
    )
}