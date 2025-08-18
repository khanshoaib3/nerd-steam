package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.ui.components.ErrorColumn
import com.github.khanshoaib3.nerdsteam.ui.components.MonochromeAsyncImage
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaTab(
    appData: AppData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(R.dimen.padding_small),
                horizontal = dimensionResource(R.dimen.padding_medium)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (appData.commonDetails?.media?.screenshots.isNullOrEmpty()) {
            ErrorColumn(
                reason = null,
                title = "No screenshots found!",
            )
        } else appData.commonDetails.let { commonAppDetails ->
            Text(
                "Screenshots",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()

            HorizontalMultiBrowseCarousel(
                state = rememberCarouselState {
                    commonAppDetails.media?.screenshots?.size ?: 0
                },
                modifier = Modifier.fillMaxWidth(),
                preferredItemWidth = 421.dp,
                itemSpacing = dimensionResource(R.dimen.padding_medium),
            ) {
                MonochromeAsyncImage(
                    model = commonAppDetails.media?.screenshots?.get(it) ?: "",
                    contentDescription = "Screenshot $it",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(16f / 9f) // 1920 x 1080
                )
            }
        }
    }
}