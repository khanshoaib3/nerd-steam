package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import com.github.khanshoaib3.steamcompanion.R

@Composable
fun TableFooter(
    modifier: Modifier = Modifier.Companion,
) {
    Row(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_very_small))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Text(
            buildAnnotatedString {
                append("powered by ")
                withLink(
                    LinkAnnotation.Url(
                        "https://steamcharts.com/",
                        TextLinkStyles(style = SpanStyle(color = Color.Blue))
                    )
                ) {
                    append("steamcharts.com")
                }
            },
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Companion.Italic,
            textAlign = TextAlign.Companion.End,
        )
    }
}