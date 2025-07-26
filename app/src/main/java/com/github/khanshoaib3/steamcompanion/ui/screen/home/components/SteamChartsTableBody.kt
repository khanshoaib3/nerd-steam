package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.SteamChartsItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T : SteamChartsItem> SteamChartsTableBody(
    modifier: Modifier = Modifier,
    gamesList: List<T>,
    imageWidth: Dp,
    imageHeight: Dp,
    onRowClick: (appId: Int) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        if (gamesList.isEmpty()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                LoadingIndicator()
            }
        } else {
            gamesList.forEach { gameItem ->
                SteamChartsTableRow(
                    modifier = Modifier,
                    item = gameItem,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight,
                    onClick = onRowClick
                )
            }
        }
    }
}
