package com.github.khanshoaib3.steamcompanion.ui.screen.home

import android.content.res.Configuration
import android.icu.text.NumberFormat
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.steamcharts.TrendingGame
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangeNegative
import com.github.khanshoaib3.steamcompanion.ui.theme.steamChartsChangePositive

@Composable
fun TrendingGamesRow(trendingGames: List<TrendingGame>, modifier: Modifier = Modifier) {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    HeaderRow(imageWidth)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        trendingGames.forEach { item ->
            TrendingGameCapsule(
                url = "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.appId}/library_600x900.jpg",
                name = item.name,
                gain = item.gain,
                players = item.currentPlayers,
                imageWidth = imageWidth,
                imageHeight = imageHeight
            )
        }
    }
}

@Composable
fun TrendingGameCapsule(
    url: String,
    name: String,
    gain: String,
    players: Int,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(url)
                .build(),
            contentDescription = name,
            placeholder = painterResource(R.drawable.preview_image_300x450),
            modifier = Modifier
                .size(width = imageWidth, height = imageHeight)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.width(4.dp))
        Text(
            name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f)
        )
        Text(
            gain,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (gain.first() == '+') steamChartsChangePositive else steamChartsChangeNegative,
            modifier = Modifier.weight(1f)
        )
        Text(
            NumberFormat.getNumberInstance().format(players),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun HeaderRow(imageWidth: Dp, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Trending Games",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start
            )
        }
        Spacer(Modifier.height(4.dp))
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
                "24-Hour Change",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Current Players",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF111318
)
@Preview(showBackground = true)
@Composable
private fun TrendingGameCapsulePreview() {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }
    SteamCompanionTheme {
        TrendingGameCapsule(
            url = "https://cdn.cloudflare.steamstatic.com/steam/apps/12150/library_600x900.jpg",
            name = "Max Payne 2: The Fall of Max Payne",
            gain = "+300%",
            players = 31,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF111318
)
@Preview(showBackground = true)
@Composable
fun TrendingGamesRowPreview() {
    SteamCompanionTheme {
        TrendingGamesRow(
            listOf(
                TrendingGame(
                    id = 1,
                    appId = 12150,
                    name = "Max Payne 2: The Fall of Max Payne",
                    gain = "-3%",
                    currentPlayers = 31
                ),
                TrendingGame(
                    id = 2,
                    appId = 367520,
                    name = "Hollow Knight",
                    gain = "+26.22%",
                    currentPlayers = 4540
                )
            )
        )
    }
}