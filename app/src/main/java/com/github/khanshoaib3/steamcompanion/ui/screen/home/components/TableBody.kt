package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.SteamChartsItem
import com.github.khanshoaib3.steamcompanion.R

@Composable
fun <T : SteamChartsItem> TableBody(
    gamesList: List<T>,
    imageWidth: Dp,
    imageHeight: Dp,
    onRowClick: (appId: Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        gamesList.forEach { item ->
            TableRow(
                item = item,
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                onClick = onRowClick,
                modifier = Modifier
            )
        }
    }
}

