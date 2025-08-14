package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.MonthlyPlayerStatisticDisplay
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.PlayerStatistics
import com.github.khanshoaib3.nerdsteam.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.nerdsteam.ui.components.ErrorColumn
import com.github.khanshoaib3.nerdsteam.ui.components.SteamChartsFooter
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppData
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppViewState
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.DataType
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme
import com.github.khanshoaib3.nerdsteam.ui.theme.steamChartsChangeNegative
import com.github.khanshoaib3.nerdsteam.ui.theme.steamChartsChangePositive
import com.github.khanshoaib3.nerdsteam.utils.Progress
import com.github.marlonlom.utilities.timeago.TimeAgo
import java.text.NumberFormat
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalTime::class)
@Composable
fun PlayerStatsTab(
    appData: AppData,
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

            is Progress.FAILED -> {
                ErrorColumn(reason = appViewState.steamChartsStatus.reason)
            }

            else -> appData.playerStatistics?.let { playerStatistics ->
                val numberFormat = NumberFormat.getInstance()
                Card(
                    Modifier.fillMaxWidth(0.95f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryFixedDim)
                ) {
                    FlowRow(
                        verticalArrangement = Arrangement.SpaceEvenly,
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
                                text = TimeAgo.using(playerStatistics.lastHourTime),
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
                            text = "Month",
                            modifier = Modifier.weight(0.9f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = "Avg. Players",
                            modifier = Modifier.weight(1.25f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Gain",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "% Gain",
                            modifier = Modifier.weight(0.9f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            "Peak Players",
                            modifier = Modifier.weight(1.25f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                    HorizontalDivider()
                    playerStatistics.perMonthPlayerStats.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it.month,
                                modifier = Modifier.weight(0.9f),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = NumberFormat.getNumberInstance().format(it.avgPlayers),
                                modifier = Modifier.weight(1.25f),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = it.gain,
                                modifier = Modifier.weight(1f),
                                color = if (it.gain.isNotEmpty() && it.gain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = it.percGain,
                                modifier = Modifier.weight(0.9f),
                                color = if (it.percGain.isNotEmpty() && it.percGain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = NumberFormat.getNumberInstance().format(it.peakPlayers),
                                modifier = Modifier.weight(1.25f),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                SteamChartsFooter(url = "https://steamcharts.com/app/${appData.steamAppId}")
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun PlayerStatsTabPreview() {
    NerdSteamTheme {
        PlayerStatsTab(
            appData = AppData(
                steamAppId = 220,
                playerStatistics = PlayerStatistics(
                    lastHourCount = 232323232,
                    lastHourTime = Instant.DISTANT_PAST.toEpochMilliseconds(),
                    twentyFourHourPeak = 232323232,
                    allTimePeak = 23232,
                    perMonthPlayerStats = listOf(
                        MonthlyPlayerStatisticDisplay(
                            month = "Last 30 Days",
                            avgPlayers = 1100.0,
                            gain = "123",
                            percGain = "12",
                            peakPlayers = 2000.0,
                        ),
                        MonthlyPlayerStatisticDisplay(
                            month = "May 2025",
                            avgPlayers = 1039662.8,
                            gain = "36092.25",
                            percGain = "+3.60%",
                            peakPlayers = 1818368.0,
                        ),
                    )
                )
            ),
            appViewState = AppViewState(steamChartsStatus = Progress.LOADED),
            fetchDataFromSourceCallback = {},
        )
    }
}