package com.github.khanshoaib3.steamcompanion.ui.screen.home.components

import android.icu.text.NumberFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.SteamChartsItem
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopGame
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TopRecord
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangeNegative
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangePositive

@Composable
fun <T : SteamChartsItem> SteamChartsTableRow(
    modifier: Modifier = Modifier,
    item: T,
    imageWidth: Dp,
    imageHeight: Dp,
    onClick: (appId: Int) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
//            .fillMaxWidth()
            .clickable(true, onClick = { onClick(item.appId) })
    ) {
        Row(
            modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(0.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    item.id.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.appId}/library_600x900.jpg",
                    contentDescription = item.name,
                    placeholder = painterResource(R.drawable.preview_image_300x450),
                    modifier = Modifier
                        .size(width = imageWidth, height = imageHeight)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                CenterAlignedSelectableText(
                    item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        when (item) {
            is TrendingGame -> TrendingGameColumns(item, Modifier.weight(1f))
            is TopGame -> TopGameColumns(item, Modifier.weight(1.2f))
            is TopRecord -> TopRecordColumns(item, Modifier.weight(1f))
            else -> throw Error("Unknown type found for item")
        }
    }
}

@Composable
fun TrendingGameColumns(
    item: TrendingGame,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        CenterAlignedSelectableText(
            item.gain,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (item.gain.first() == '+') steamChartsChangePositive else steamChartsChangeNegative,
            modifier = Modifier.weight(0.8f)
        )
        CenterAlignedSelectableText(
            NumberFormat.getNumberInstance().format(item.currentPlayers),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TopGameColumns(
    item: TopGame,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        CenterAlignedSelectableText(
            NumberFormat.getNumberInstance().format(item.currentPlayers),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        CenterAlignedSelectableText(
            NumberFormat.getNumberInstance().format(item.peakPlayers),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.1f)
        )
        CenterAlignedSelectableText(
            NumberFormat.getNumberInstance().format(item.hours),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.3f)
        )
    }
}

@Composable
fun TopRecordColumns(
    item: TopRecord,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        CenterAlignedSelectableText(
            NumberFormat.getNumberInstance().format(item.peakPlayers),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        CenterAlignedSelectableText(
            item.month,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.8f)
        )
    }
}

//@Preview(
//    showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF111318
//)
@Preview(showBackground = true)
@Composable
private fun TrendingGameRowPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    SteamCompanionTheme {
        SteamChartsTableRow(
            item = TrendingGame(
                id = 10,
                appId = 12150,
                name = "Max Payne 2: The Fall of Max Payne",
                currentPlayers = 31,
                gain = "+351%"
            ), imageWidth = imageWidth, imageHeight = imageHeight
        )
    }
}

//@Preview(
//    showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF111318
//)
@Preview(showBackground = true)
@Composable
private fun TopGameRowPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    SteamCompanionTheme {
        SteamChartsTableRow(
            item = TopGame(
                id = 10,
                appId = 12150,
                name = "Max Payne 2: The Fall of Max Payne",
                peakPlayers = 315,
                currentPlayers = 31,
                hours = 729111667
            ), imageWidth = imageWidth, imageHeight = imageHeight
        )
    }
}

//@Preview(
//    showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF111318
//)
@Preview(showBackground = true)
@Composable
private fun TopRecordRowPreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    SteamCompanionTheme {
        SteamChartsTableRow(
            item = TopRecord(
                id = 10,
                appId = 12150,
                name = "Max Payne 2: The Fall of Max Payne",
                peakPlayers = 315,
                month = "Jan 2018"
            ), imageWidth = imageWidth, imageHeight = imageHeight
        )
    }
}
