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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorColumn(
    reason: String?,
    modifier: Modifier = Modifier,
    title: String = "Unable to load!",
    titleStyle: TextStyle = MaterialTheme.typography.bodyLargeEmphasized,
    reasonStyle: TextStyle = MaterialTheme.typography.bodySmall,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.ErrorOutline, "Error")
        Text(
            text = title,
            style = titleStyle,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        if (!reason.isNullOrBlank()) {
            Text(
                text = reason,
                style = reasonStyle,
                textAlign = TextAlign.Center,
            )
        }
    }
}