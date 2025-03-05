package com.github.khanshoaib3.steamcompanion.ui.screen.home

import android.content.res.Configuration
import android.icu.text.NumberFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.ui.common.CollapseButton
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import kotlin.math.exp

@Composable
fun TopRecordsRow(
    topRecords: List<TopRecord>,
    expanded: Boolean,
    collapseButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_very_small))
    ) {
        TopRecordsHeader(expanded, imageWidth, collapseButtonOnClick)
        if (expanded) {
            HorizontalDivider(
                Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_very_small)
                )
            )
            topRecords.forEach { item ->
                TopRecordCapsule(
                    topRecord = item, imageWidth = imageWidth, imageHeight = imageHeight
                )
            }
        }
    }
}

@Composable
fun TopRecordCapsule(
    topRecord: TopRecord, imageWidth: Dp, imageHeight: Dp, modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("https://cdn.cloudflare.steamstatic.com/steam/apps/${topRecord.appId}/library_600x900.jpg")
                .build(),
            contentDescription = topRecord.name,
            placeholder = painterResource(R.drawable.preview_image_300x450),
            modifier = Modifier
                .size(width = imageWidth, height = imageHeight)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(
            topRecord.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f)
        )
        Text(
            NumberFormat.getNumberInstance().format(topRecord.peakPlayers),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Text(
            topRecord.month,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TopRecordsHeader(
    expanded: Boolean,
    imageWidth: Dp,
    collapseButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(true, onClick = collapseButtonOnClick)
        ) {
            Text(
                "Top Records",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start
            )
            CollapseButton(expanded, collapseButtonOnClick)
        }
        if (expanded) {
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Row {
                Spacer(Modifier.width(imageWidth))
                Text(
                    "App Name",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    "Peak Players",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Time",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(
    showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF111318
)
@Preview(showBackground = true)
@Composable
private fun TopRecordCapsulePreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    SteamCompanionTheme {
        TopRecordCapsule(
            topRecord = TopRecord(
                appId = 12150,
                name = "Max Payne 2: The Fall of Max Payne",
                peakPlayers = 315,
                month = "Jan 2018"
            ), imageWidth = imageWidth, imageHeight = imageHeight
        )
    }
}

@Preview(
    showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF111318
)
@Preview(showBackground = true)
@Composable
fun TopRecordsRowPreview() {
    SteamCompanionTheme {
        TopRecordsRow(topRecords = listOf(
            TopRecord(
                id = 1,
                appId = 12150,
                name = "Max Payne 2: The Fall of Max Payne",
                peakPlayers = 351,
                month = "Jan 2018"
            ), TopRecord(
                id = 2,
                appId = 367520,
                name = "Hollow Knight",
                peakPlayers = 20169,
                month = "Mar 2016"
            )
        ), expanded = true, collapseButtonOnClick = {})
    }
}