package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.SteamChartsItem

@Composable
fun <T : SteamChartsItem>TableBody(
    gamesList: List<T>,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier.Companion,
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
    ) {
        gamesList.forEach { item ->
            TableRow(
                item = item,
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                modifier = Modifier.Companion.padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

