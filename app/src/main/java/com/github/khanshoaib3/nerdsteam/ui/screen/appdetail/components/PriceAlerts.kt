package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.nerdsteam.R
import kotlin.text.toFloat

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PriceAlertRow(
    onClick: () -> Unit,
    alertAlreadySet: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (alertAlreadySet) {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Edit Price Alert",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLargeEmphasized
                )
            }
        } else {
            FilledIconButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Set Price Alert",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLargeEmphasized
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceAlertSheet(
    sheetState: SheetState,
    targetPrice: Float,
    maxPrice: Float,
    onPriceChange: (Float) -> Unit,
    selectedNotificationOptionIndex: Int,
    notificationOptions: List<String>,
    onSelectedNotificationOptionIndexChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    alertAlreadySet: Boolean,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        PriceAlertSheetContent(
            targetPrice = targetPrice,
            maxPrice = maxPrice,
            onPriceChange = onPriceChange,
            selectedNotificationOptionIndex = selectedNotificationOptionIndex,
            notificationOptions = notificationOptions,
            onSelectedNotificationOptionIndexChange = onSelectedNotificationOptionIndexChange,
            onCancel = onCancel,
            onConfirm = onConfirm,
            alertAlreadySet = alertAlreadySet,
            onStop = onStop,
            modifier = modifier
        )
    }
}

@Composable
fun PriceAlertSheetContent(
    targetPrice: Float,
    maxPrice: Float,
    onPriceChange: (Float) -> Unit,
    selectedNotificationOptionIndex: Int,
    notificationOptions: List<String>,
    onSelectedNotificationOptionIndexChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    alertAlreadySet: Boolean,
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
        if (alertAlreadySet) {
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
                Text(
                    "Cancel",
                    textAlign = TextAlign.Center,
                )
            }
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    if (alertAlreadySet) "Update" else "Set Alert",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PriceAlertSheetPreview() {
    PriceAlertSheetContent(
        targetPrice = 45.0f,
        maxPrice = 199.99f,
        onPriceChange = {},
        selectedNotificationOptionIndex = 1,
        notificationOptions = listOf("Everyday", "Once"),
        onSelectedNotificationOptionIndexChange = {},
        onCancel = {},
        onConfirm = {},
        alertAlreadySet = false,
        onStop = {}
    )
}
