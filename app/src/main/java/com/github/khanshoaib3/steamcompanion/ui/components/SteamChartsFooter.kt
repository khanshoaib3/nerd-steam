package com.github.khanshoaib3.steamcompanion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink

@Composable
fun SteamChartsFooter(modifier: Modifier = Modifier, url: String = "https://steamcharts.com/") {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            // https://developer.android.com/reference/kotlin/androidx/compose/ui/text/LinkAnnotation
            buildAnnotatedString {
                append("powered by ")
                withLink(
                    link = LinkAnnotation.Url(
                        url = url,
                        styles = TextLinkStyles(style = SpanStyle(color = Color.Blue))
                    )
                ) {
                    append("steamcharts.com")
                }
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
        )
    }
}