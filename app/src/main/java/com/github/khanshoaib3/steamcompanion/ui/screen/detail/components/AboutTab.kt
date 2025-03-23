package com.github.khanshoaib3.steamcompanion.ui.screen.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.GameData

@Composable
fun AboutTab(modifier: Modifier = Modifier, gameData: GameData) {
    Column(modifier = modifier) {
        Text(
            AnnotatedString.fromHtml(
                gameData.content?.data?.detailedDescription ?: "<b>Empty</b>",
                linkStyles = TextLinkStyles(style = SpanStyle(color = Color.Blue))
            )
        )
    }
}
