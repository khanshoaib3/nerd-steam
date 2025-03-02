package com.github.khanshoaib3.steamcompanion.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
    Card(modifier = modifier) {
        Column() {
            GameEntry(
                url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2870990/f30d4cd8ade1620624d6b12c169d970e68b95616/capsule_231x87.jpg",
                name = "Mecha BREAK Demo",
                players = 91851
            )
            GameEntry(
                url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/3061810/99ff0f2acf35c829ffea33c2c463beae64999b3a/capsule_231x87.jpg",
                name = "Like a Dragon: Pirate Yakuza in Hawaii",
                players = 7459
            )
            GameEntry(
                url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/3241660/68eff6f7de678798ac2adb040c8bb73025549c79/capsule_231x87.jpg",
                name = "R.E.P.O.",
                players = 10306
            )
            GameEntry(
                url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/3487890/faa8e1846f37709dc59da198a44a5635e932f91f/capsule_231x87.jpg",
                name = "Fellowship Demo",
                players = 6045
            )
            GameEntry(
                url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/3456580/7ce99650fb029708a582cf1a9d4b483d09466af6/capsule_231x87.jpg",
                name = "RoadCraft Demo",
                players = 2178
            )
        }
    }
}

@Composable
fun GameEntry(url: String, name: String, players: Int, modifier: Modifier = Modifier) {
    val density: Density = LocalDensity.current
    val width: Dp
    val height: Dp
    with(density) {
        width = 347.toDp()
        height = 131.toDp()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(url)
                .build(),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = width, height = height)
                .weight(1f)
        )
        Text(name, modifier = Modifier.weight(2f))
        Text(players.toString(), modifier = Modifier.weight(1f))
        Spacer(modifier.height(2.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun GameEntryPreview() {
    SteamCompanionTheme {
        GameEntry(
            url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2870990/f30d4cd8ade1620624d6b12c169d970e68b95616/capsule_231x87.jpg",
            name = "Mecha BREAK Demo",
            players = 91851
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeViewPreview() {
    SteamCompanionTheme {
        HomeView()
    }
}