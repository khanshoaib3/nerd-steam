package com.github.khanshoaib3.nerdsteam.ui.screen.pricealerts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.ui.screen.pricealerts.PriceAlertDisplay

@Composable
fun PriceAlertTable(
    alerts: List<PriceAlertDisplay>,
    onGameClick: (Int) -> Unit,
    onNameHeaderClick: () -> Unit,
    onCurrentPriceHeaderClick: () -> Unit,
    onTargetPriceHeaderClick: () -> Unit,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (alerts.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No alerts set!")
            }
        } else {
            PriceAlertTableHeader(
                onNameHeaderClick = onNameHeaderClick,
                onCurrentPriceHeaderClick = onCurrentPriceHeaderClick,
                onTargetPriceHeaderClick = onTargetPriceHeaderClick,
            )
            HorizontalDivider(
                Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_very_small)
                )
            )
            PriceAlertTableBody(
                alerts = alerts,
                onGameClick = onGameClick,
                imageWidth = imageWidth,
                imageHeight = imageHeight
            )
        }
    }
}
