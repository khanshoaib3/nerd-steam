package com.github.khanshoaib3.steamcompanion.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun PaddingValues.removeBottomPadding(layoutDirection: LayoutDirection = LayoutDirection.Ltr): PaddingValues {
    val start = this.calculateStartPadding(layoutDirection)
    val top = this.calculateTopPadding()
    val end = this.calculateEndPadding(layoutDirection)
    return PaddingValues(start = start, top = top, end = end, bottom = 0.dp)
}
