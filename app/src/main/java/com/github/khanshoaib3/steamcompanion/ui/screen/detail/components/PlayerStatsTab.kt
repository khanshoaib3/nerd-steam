package com.github.khanshoaib3.steamcompanion.ui.screen.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
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
                CenterAlignedSelectableText(
                    "Month",
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                CenterAlignedSelectableText(
                    "Avg. Players",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                CenterAlignedSelectableText(
                    "Gain",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                CenterAlignedSelectableText(
                    "% Gain",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                CenterAlignedSelectableText(
                    "Peak Players",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            HorizontalDivider()
            appData.playerStatsRowData.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CenterAlignedSelectableText(
                        it.month,
                        modifier = Modifier.weight(1.5f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    CenterAlignedSelectableText(
                        it.avgPlayers,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    CenterAlignedSelectableText(
                        it.gain,
                        modifier = Modifier.weight(1f),
                        color = if (it.gain.isNotEmpty() && it.gain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    CenterAlignedSelectableText(
                        it.percGain,
                        modifier = Modifier.weight(1f),
                        color = if (it.percGain.isNotEmpty() && it.percGain.first() != '-') steamChartsChangePositive else steamChartsChangeNegative,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    CenterAlignedSelectableText(
                        it.peakPlayers,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
