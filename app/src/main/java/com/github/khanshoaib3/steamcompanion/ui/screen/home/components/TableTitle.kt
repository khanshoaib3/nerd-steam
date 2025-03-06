package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun TableTitle(
    tableType: SteamChartsTableType,
    isTableExpanded: Boolean = true,
    collapseButtonOnClick: () -> Unit = {},
    modifier: Modifier = Modifier.Companion,
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(true, onClick = collapseButtonOnClick)
            .padding(horizontal = dimensionResource(R.dimen.padding_small))
    ) {
        Text(
            text = when (tableType) {
                SteamChartsTableType.TrendingGames -> "Trending Games"
                SteamChartsTableType.TopGames -> "Top Games"
                SteamChartsTableType.TopRecords -> "Top Records"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Companion.Light,
            textAlign = TextAlign.Companion.Start
        )
        CollapseButton(expanded = isTableExpanded, onClick = collapseButtonOnClick)
    }
}