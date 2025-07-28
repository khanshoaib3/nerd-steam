package com.github.khanshoaib3.steamcompanion.ui.screen.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppData
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.AppViewState
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.DataSourceType
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.Progress
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangeNegative
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangePositive

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlayerStatsTab(
    appData: AppData,
    appViewState: AppViewState,
    fetchDataFromSourceCallback: (DataSourceType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(R.dimen.padding_small),
                horizontal = dimensionResource(R.dimen.padding_medium)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (appViewState.steamChartsFetchStatus != Progress.LOADED) {
            LaunchedEffect(appViewState) {
                fetchDataFromSourceCallback(DataSourceType.STEAM_CHARTS)
            }

            LoadingIndicator()
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Month",
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Avg. Players",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Gain",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "% Gain",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Peak Players",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            HorizontalDivider()
            LazyColumn {
                items(appData.playerStatsRowData) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            it.month,
                            modifier = Modifier.weight(1.5f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            it.avgPlayers,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            it.gain,
                            modifier = Modifier.weight(1f),
                            color = if (it.gain.isNotEmpty() && it.gain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            it.percGain,
                            modifier = Modifier.weight(1f),
                            color = if (it.percGain.isNotEmpty() && it.percGain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            it.peakPlayers,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}
