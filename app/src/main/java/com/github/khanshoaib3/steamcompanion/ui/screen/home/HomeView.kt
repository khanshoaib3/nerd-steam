package com.github.khanshoaib3.steamcompanion.ui.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import com.github.khanshoaib3.steamcompanion.R;
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.khanshoaib3.steamcompanion.ui.AppViewModelProvider
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Card {
            Column {
                homeUiState.trendingGames.forEach { item ->
                    GameEntry(
                        url = "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.appId}/library_600x900.jpg",
                        name = item.name,
                        players = item.currentPlayers
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Card {
            Column {
                homeUiState.topGames.forEach { item ->
                    GameEntry(
                        url = "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.appId}/library_600x900.jpg",
                        name = item.name,
                        players = item.currentPlayers
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Card {
            Column {
                homeUiState.topRecords.forEach { item ->
                    GameEntry(
                        url = "https://cdn.cloudflare.steamstatic.com/steam/apps/${item.appId}/library_600x900.jpg",
                        name = item.name,
                        players = item.peakPlayers
                    )
                }
            }
        }
    }
}

@Composable
fun GameEntry(url: String, name: String, players: Int, modifier: Modifier = Modifier) {
    val density: Density = LocalDensity.current
    val width: Dp
    val height: Dp
    with(density) {
        width = 225.toDp()
        height = 338.toDp()
    }
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(url)
                    .build(),
                contentDescription = name,
                placeholder = painterResource(R.drawable.preview_image_300x450),
                modifier = Modifier
                    .size(width = width, height = height)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(name, modifier = Modifier.weight(2f))
            Text(players.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier.height(2.dp))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun GameEntryPreview() {
    SteamCompanionTheme {
        GameEntry(
            url = "https://cdn.cloudflare.steamstatic.com/steam/apps/12150/library_600x900.jpg",
            name = "Max Payne 2: The Fall of Max Payne",
            players = 91851
        )
    }
}

@Composable
private fun HomeViewPreview() {
    SteamCompanionTheme {
        HomeView()
    }
}