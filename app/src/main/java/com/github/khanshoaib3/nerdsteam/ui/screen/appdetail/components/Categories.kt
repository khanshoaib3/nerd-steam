package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.Category
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Categories(
    modifier: Modifier = Modifier,
    categories: List<Category>?,
) {
    if (categories == null || categories.isEmpty()) return

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_very_small))
            ) {
                categories.forEach { category ->
                    CategoryChip(
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
                        category = category,
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(modifier: Modifier = Modifier, category: Category) {
    var isOpen by remember { mutableStateOf(false) }
    val view = LocalView.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(true, onClick = {
                isOpen = !isOpen
                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            })
            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
            .background(color = if (isOpen) MaterialTheme.colorScheme.secondary else Color.Unspecified)
            .animateContentSize()
    ) {
        AsyncImage(
            model = category.url,
            contentDescription = category.name,
            placeholder = painterResource(R.drawable.windows_icon),
            modifier = Modifier
                .size(28.dp)
                .padding(start = dimensionResource(R.dimen.padding_very_small))
        )
        AnimatedVisibility(isOpen) {
            Text(
                category.name,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_very_small))
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameDetailScreenPreview() {
    NerdSteamTheme {
        CategoryChip(category = Category(url = "https://steamdb.info/static/img/categories/2.png", name = "Single-player"))
    }
}
