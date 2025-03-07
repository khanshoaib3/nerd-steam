package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.steamcompanion.R

@Composable
fun TableHeader(
    tableType: SteamChartsTableType,
    imageWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val headingsWithWeights: List<Pair<String, Float>> = when (tableType) {
        SteamChartsTableType.TrendingGames -> listOf(
            Pair("24-Hour Change", 1f),
            Pair("Current Players", 1f),
        )

        SteamChartsTableType.TopGames -> listOf(
            Pair("Current Players", 1f),
            Pair("Peak Players", 1f),
            Pair("Hours Played", 1.25f),
        )

        SteamChartsTableType.TopRecords -> listOf(
            Pair("Peak Players", 1f),
            Pair("Time", 1f),
        )
    }

    Column(modifier = modifier) {
        Row {
            Row(modifier = Modifier.weight(1f)) {
                Spacer(Modifier.width(imageWidth))
                Text(
                    "App Name",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                headingsWithWeights.forEach {
                    Text(
                        it.first,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.weight(it.second)
                    )
                }
            }
        }
        HorizontalDivider(
            Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_very_small)
            )
        )
    }
}