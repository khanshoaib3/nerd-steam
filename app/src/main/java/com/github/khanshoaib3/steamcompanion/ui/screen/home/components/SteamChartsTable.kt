package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.SteamChartsItem
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.steamcompanion.ui.components.SteamChartsFooter
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme

enum class SteamChartsTableType {
    TrendingGames,
    TopGames,
    TopRecords
}

@Composable
fun <T : SteamChartsItem> SteamChartsTable(
    modifier: Modifier = Modifier,
    gamesList: List<T>,
    tableType: SteamChartsTableType,
    isTableExpanded: Boolean = true,
    onCollapseButtonClick: () -> Unit = {},
    onGameRowClick: (appId: Int) -> Unit = {}
) {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_small)
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_very_small))
        ) {
            SteamChartsTableTitle(
                tableType = tableType,
                isTableExpanded = isTableExpanded,
                onCollapseButtonClick = onCollapseButtonClick
            )
            AnimatedVisibility(visible = isTableExpanded) {
                Column {
                    SteamChartsTableHeader(tableType = tableType)
                    SteamChartsTableBody(
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)),
                        gamesList = gamesList,
                        imageWidth = imageWidth,
                        imageHeight = imageHeight,
                        onRowClick = onGameRowClick
                    )
                    SteamChartsFooter(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_very_small)))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrendingGamesTablePreview() {
    SteamCompanionTheme {
        SteamChartsTable(
            gamesList = listOf(
                TrendingGame(
                    id = 1,
                    appId = 12150,
                    name = "Max Payne 2: The Fall of Max Payne",
                    gain = "-3%",
                    currentPlayers = 31
                ), TrendingGame(
                    id = 2,
                    appId = 367520,
                    name = "Hollow Knight",
                    gain = "+26.22%",
                    currentPlayers = 4540
                )
            ),
            tableType = SteamChartsTableType.TrendingGames,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopGamesTablePreview() {
    SteamCompanionTheme {
        SteamChartsTable(
            gamesList = listOf(
                TopGame(
                    id = 1,
                    appId = 12150,
                    name = "Max Payne 2: The Fall of Max Payne",
                    peakPlayers = 351,
                    currentPlayers = 31,
                    hours = 729111667
                ), TopGame(
                    id = 2,
                    appId = 367520,
                    name = "Hollow Knight",
                    peakPlayers = 20169,
                    currentPlayers = 4540,
                    hours = 310064845
                )
            ),
            tableType = SteamChartsTableType.TopGames,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SteamChartsTablePreview() {
    SteamCompanionTheme {
        SteamChartsTable(
            gamesList = listOf(
                TopRecord(
                    id = 1,
                    appId = 12150,
                    name = "Max Payne 2: The Fall of Max Payne",
                    peakPlayers = 351,
                    month = "Jan 2018"
                ), TopRecord(
                    id = 2,
                    appId = 367520,
                    name = "Hollow Knight",
                    peakPlayers = 20169,
                    month = "Mar 2016"
                )
            ),
            tableType = SteamChartsTableType.TopRecords,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}