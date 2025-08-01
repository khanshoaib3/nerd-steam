package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.CollatedAppData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaTab(
    collatedAppData: CollatedAppData,
    modifier: Modifier = Modifier,
) = collatedAppData.commonDetails?.let { commonAppDetails ->
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(R.dimen.padding_small),
            horizontal = dimensionResource(R.dimen.padding_medium)
        ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if ((commonAppDetails.media?.screenshots?.size ?: 0) <= 0) return null

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
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = commonAppDetails.media?.screenshots?.get(it) ?: "",
                contentDescription = null, // TODO Add description
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}