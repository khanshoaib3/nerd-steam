package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.khanshoaib3.steamcompanion.R

@Composable
fun TableHeader(
    tableType: SteamChartsTableType,
    modifier: Modifier = Modifier,
) {
    val headingsWithWeights: List<Pair<String, Float>> = when (tableType) {
        SteamChartsTableType.TrendingGames -> listOf(
            Pair("24-Hour Change", 0.8f),
            Pair("Current Players", 1f),
        )

        SteamChartsTableType.TopGames -> listOf(
            Pair("Current Players", 1f),
            Pair("Peak Players", 1.1f),
            Pair("Hours Played", 1.3f),
        )

        SteamChartsTableType.TopRecords -> listOf(
            Pair("Peak Players", 1f),
            Pair("Time", 0.8f),
        )
    }

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "Game",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                )
            }
            Row(
                modifier = Modifier.weight(if (tableType == SteamChartsTableType.TopGames) 1.2f else 1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
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