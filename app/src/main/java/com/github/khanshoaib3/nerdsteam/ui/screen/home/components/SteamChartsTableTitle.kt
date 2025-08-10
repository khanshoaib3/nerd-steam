package com.github.khanshoaib3.nerdsteam.ui.screen.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.khanshoaib3.nerdsteam.ui.components.CollapseButton

@Composable
fun SteamChartsTableTitle(
    tableType: SteamChartsTableType,
    modifier: Modifier = Modifier,
    isTableExpanded: Boolean = true,
    onCollapseButtonClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(true, onClick = onCollapseButtonClick)
    ) {
        Text(
            text = when (tableType) {
                SteamChartsTableType.TrendingGames -> "Trending Games"
                SteamChartsTableType.TopGames -> "Top Games"
                SteamChartsTableType.TopRecords -> "Top Records"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Start
        )
        CollapseButton(expanded = isTableExpanded, onClick = onCollapseButtonClick)
    }
}