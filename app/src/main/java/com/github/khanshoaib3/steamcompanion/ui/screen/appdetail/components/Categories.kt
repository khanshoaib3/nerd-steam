package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.github.khanshoaib3.steamcompanion.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Categories(
    modifier: Modifier = Modifier,
    categories: List<com.github.khanshoaib3.steamcompanion.data.model.appdetail.Category>?,
) {
    if (categories == null || categories.isEmpty()) return

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_very_small))
            ) {
                categories.forEach { category ->
                    CategoryChip(
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
                        category = category,
                    )
                }
            }
        }
    }
}
