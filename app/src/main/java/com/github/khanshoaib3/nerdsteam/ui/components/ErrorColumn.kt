package com.github.khanshoaib3.nerdsteam.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorColumn(
    reason: String?,
    modifier: Modifier = Modifier,
    title: String = "Unable to load!",
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.ErrorOutline, "Error")
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLargeEmphasized,
            fontWeight = FontWeight.Bold,
        )
        if (!reason.isNullOrBlank()) {
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}