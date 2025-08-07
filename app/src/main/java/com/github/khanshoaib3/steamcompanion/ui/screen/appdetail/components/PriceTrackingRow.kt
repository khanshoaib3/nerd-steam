package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.steamcompanion.R
import kotlin.text.toFloat

@Composable
fun PriceTrackingRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = "Set Price Alert",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceTrackingSheet(
    sheetState: SheetState,
    targetPrice: Float,
    maxPrice: Float,
    onPriceChange: (Float) -> Unit,
    selectedNotificationOptionIndex: Int,
    notificationOptions: List<String>,
    onSelectedNotificationOptionIndexChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    priceAlreadyTracked: Boolean,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        PriceTrackingSheetContent(
            targetPrice = targetPrice,
            maxPrice = maxPrice,
            onPriceChange = onPriceChange,
            selectedNotificationOptionIndex = selectedNotificationOptionIndex,
            notificationOptions = notificationOptions,
            onSelectedNotificationOptionIndexChange = onSelectedNotificationOptionIndexChange,
            onCancel = onCancel,
            onConfirm = onConfirm,
            priceAlreadyTracked = priceAlreadyTracked,
            onStop = onStop,
            modifier = modifier
        )
    }
}

@Composable
fun PriceTrackingSheetContent(
    targetPrice: Float,
    maxPrice: Float,
    onPriceChange: (Float) -> Unit,
    selectedNotificationOptionIndex: Int,
    notificationOptions: List<String>,
    onSelectedNotificationOptionIndexChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    priceAlreadyTracked: Boolean,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Target Price",
                    modifier = Modifier.weight(0.75f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    targetPrice.toString(),
                    onValueChange = { onPriceChange(it.toFloat()) },
                    modifier = Modifier.weight(0.25f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Slider(
                value = targetPrice,
                onValueChange = onPriceChange,
                valueRange = 0f..maxPrice,
//                steps = (maxPrice / 20).toInt()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Frequency",
                    modifier = Modifier.weight(0.75f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                SingleChoiceSegmentedButtonRow {
                    notificationOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = notificationOptions.size
                            ),
                            onClick = { onSelectedNotificationOptionIndexChange(index) },
                            selected = index == selectedNotificationOptionIndex,
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
        if (priceAlreadyTracked) {
            Spacer(Modifier.height(32.dp))
            FilledTonalButton(onClick = onStop, modifier = Modifier.fillMaxWidth(0.9f)) {
                Text("Remove Alert")
            }
            Spacer(Modifier.height(24.dp))
        } else {
            Spacer(Modifier.height(64.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(if (priceAlreadyTracked) "Update" else "Start Tracking")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PriceTrackingSheetPreview() {
    PriceTrackingSheetContent(
        targetPrice = 45.0f,
        maxPrice = 199.99f,
        onPriceChange = {},
        selectedNotificationOptionIndex = 1,
        notificationOptions = listOf("Everyday", "Once"),
        onSelectedNotificationOptionIndexChange = {},
        onCancel = {},
        onConfirm = {},
        priceAlreadyTracked = true,
        onStop = {}
    )
}
