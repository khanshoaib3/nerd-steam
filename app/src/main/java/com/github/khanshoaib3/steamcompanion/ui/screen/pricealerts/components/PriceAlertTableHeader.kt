package com.github.khanshoaib3.steamcompanion.ui.screen.pricealerts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PriceAlertTableHeader(
    onNameHeaderClick: () -> Unit,
    onPriceHeaderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO Add a11y indication of sorting order
    Row(modifier) {
        Row(
            Modifier
                .weight(0.6f)
                .clickable(true, onClick = onNameHeaderClick, role = Role.Button),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Game",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .weight(0.3f)
                .clickable(true, onClick = onPriceHeaderClick, role = Role.Button),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Current Price",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier.weight(0.15f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "AppId",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }

}

