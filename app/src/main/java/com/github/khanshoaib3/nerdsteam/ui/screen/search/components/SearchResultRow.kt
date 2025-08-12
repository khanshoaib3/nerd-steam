package com.github.khanshoaib3.nerdsteam.ui.screen.search.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.nerdsteam.ui.screen.search.AppSearchResultDisplay
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchResultRow(
    searchResult: AppSearchResultDisplay,
    imageWidth: Dp,
    imageHeight: Dp,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.clickable(
            enabled = true,
            onClick = { onClick(searchResult.appId) },
            role = Role.Button
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier.weight(0.6f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = searchResult.iconUrl,
                contentDescription = searchResult.name,
                placeholder = painterResource(R.drawable.preview_image_300x450),
                modifier = Modifier
                    .size(width = imageWidth, height = imageHeight)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
            )
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Column {
                CenterAlignedSelectableText(
                    text = searchResult.name,
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                CenterAlignedSelectableText(
                    text = searchResult.appId.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchResultRowPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    NerdSteamTheme {
        SearchResultRow(
            searchResult = AppSearchResultDisplay(
                appId = 12342,
                name = "Max Payne 2: The Fall of Max Payne",
                iconUrl = "bru"
            ),
            onClick = {},
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}
