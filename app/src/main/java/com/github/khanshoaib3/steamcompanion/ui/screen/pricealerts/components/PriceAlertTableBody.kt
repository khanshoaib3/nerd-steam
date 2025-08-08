package com.github.khanshoaib3.steamcompanion.ui.screen.pricealerts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.screen.pricealerts.PriceAlertDisplay
import com.github.khanshoaib3.steamcompanion.utils.getNumberFormatFromCurrencyCode

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PriceAlertTableBody(
    modifier: Modifier = Modifier,
    alerts: List<PriceAlertDisplay>,
    onGameClick: (Int) -> Unit,
    imageWidth: Dp,
    imageHeight: Dp
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(alerts) { alert ->
            val currencyFormatter = getNumberFormatFromCurrencyCode(alert.currency)
            Row(
                Modifier.clickable(true, onClick = { onGameClick(alert.appId) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier.weight(0.6f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = alert.imageUrl,
                        contentDescription = alert.name,
                        placeholder = painterResource(R.drawable.preview_image_300x450),
                        modifier = Modifier
                            .size(width = imageWidth, height = imageHeight)
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    )
                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                    CenterAlignedSelectableText(
                        text = alert.name,
                        style = MaterialTheme.typography.bodyLargeEmphasized,
                        fontWeight = FontWeight.Bold,
                    )
                }
                CenterAlignedSelectableText(
                    modifier = Modifier.weight(0.3f),
                    text = currencyFormatter.format(alert.currentPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                CenterAlignedSelectableText(
                    modifier = Modifier.weight(0.15f),
                    text = alert.appId.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

