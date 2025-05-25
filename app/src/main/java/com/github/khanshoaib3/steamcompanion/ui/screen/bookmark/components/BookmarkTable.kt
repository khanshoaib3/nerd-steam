package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.screen.bookmark.BookmarkDisplay

@Composable
fun BookmarkTable(
    bookmarks: List<BookmarkDisplay>,
    onGameClick: (Int) -> Unit,
    onGameHeaderClick: () -> Unit,
    onTimeHeaderClick: () -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookmarkTableHeader(
            onGameHeaderClick = onGameHeaderClick,
            onTimeHeaderClick = onTimeHeaderClick
        )
        HorizontalDivider(
            Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_very_small)
            )
        )
        BookmarkTableBody(
            bookmarks = bookmarks,
            onGameClick = onGameClick,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}
