package com.github.khanshoaib3.nerdsteam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorColumn(
    reason: String?,
    modifier: Modifier = Modifier,
    title: String = "Something went wrong!",
    titleStyle: TextStyle = MaterialTheme.typography.bodyLargeEmphasized,
    reasonStyle: TextStyle = MaterialTheme.typography.bodySmall,
    iconSize: Dp = 28.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error icon",
            modifier = Modifier.size(iconSize)
        )
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