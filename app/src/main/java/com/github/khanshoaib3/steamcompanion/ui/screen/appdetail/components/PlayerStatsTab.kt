package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.components.SteamChartsFooter
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.AppViewState
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.CollatedAppData
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.DataType
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.Progress
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangeNegative
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangePositive
import com.github.marlonlom.utilities.timeago.TimeAgo
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlayerStatsTab(
    collatedAppData: CollatedAppData,
    appViewState: AppViewState,
    fetchDataFromSourceCallback: (DataType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (appViewState.steamChartsStatus) {
            Progress.NOT_QUEUED -> {
                LaunchedEffect(appViewState) {
                    fetchDataFromSourceCallback(DataType.STEAM_CHARTS_PLAYER_STATS)
                }
            }

            Progress.LOADING -> {
                LoadingIndicator()
            }

            Progress.FAILED -> {
                Icon(Icons.Default.ErrorOutline, "Error")
                Text("Unable to load!", style = MaterialTheme.typography.bodyMediumEmphasized)
            }

            else -> collatedAppData.playerStatistics?.let { playerStatistics ->
                val numberFormat = NumberFormat.getInstance()
                Card(
                    Modifier.fillMaxWidth(0.95f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryFixedDim)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_small)),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CenterAlignedSelectableText(
                                text = numberFormat.format(playerStatistics.lastHourCount),
                                style = MaterialTheme.typography.headlineMediumEmphasized
                            )
                            Text(
                                text = TimeAgo.using(playerStatistics.lastHourTime.toEpochMilli()),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CenterAlignedSelectableText(
                                text = numberFormat.format(playerStatistics.twentyFourHourPeak),
                                style = MaterialTheme.typography.headlineMediumEmphasized
                            )
                            Text(
                                text = "24 Hour Peak",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CenterAlignedSelectableText(
                                text = numberFormat.format(playerStatistics.allTimePeak),
                                style = MaterialTheme.typography.headlineMediumEmphasized
                            )
                            Text(
                                text = "All Time Peak",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Month",
                            modifier = Modifier.weight(1.5f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Avg. Players",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Gain",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "% Gain",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Peak Players",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    HorizontalDivider()
                    playerStatistics.perMonthPlayerStats.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                it.month,
                                modifier = Modifier.weight(1.5f),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                it.avgPlayers,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                it.gain,
                                modifier = Modifier.weight(1f),
                                color = if (it.gain.isNotEmpty() && it.gain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                it.percGain,
                                modifier = Modifier.weight(1f),
                                color = if (it.percGain.isNotEmpty() && it.percGain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                it.peakPlayers,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                SteamChartsFooter(url = "https://steamcharts.com/app/${collatedAppData.steamAppId}")
            }
        }
    }
}
