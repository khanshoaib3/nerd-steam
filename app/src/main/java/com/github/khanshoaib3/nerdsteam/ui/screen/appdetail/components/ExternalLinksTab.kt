package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppData
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme
import com.github.khanshoaib3.nerdsteam.utils.OpenWebPage

@Composable
fun ExternalLinksTab(
    appData: AppData,
    modifier: Modifier = Modifier,
) {
    val links = buildList {
        add(Triple(
            "Steam",
            "store.steampowered.com/app/${appData.steamAppId}/",
            "https://store.steampowered.com/app/${appData.steamAppId}/"
        ))
        add(Triple(
            "IsThereAnyDeal",
            "isthereanydeal.com/game/${appData.isThereAnyDealSlug}/",
            "https://isthereanydeal.com/game/${appData.isThereAnyDealSlug}/",
        ))
        add(Triple(
            "SteamCharts",
            "steamcharts.com/app/${appData.steamAppId}/",
            "https://steamcharts.com/app/${appData.steamAppId}/",
        ))
        add(Triple(
            "SteamDB",
            "steamdb.info/app/${appData.steamAppId}/",
            "https://steamdb.info/app/${appData.steamAppId}/",
        ))
        add(Triple(
            "ProtonDB",
            "protondb.com/app/${appData.steamAppId}/",
            "https://protondb.com/app/${appData.steamAppId}/"
        ))
        add(Triple(
            "Steam Community",
            "steamcommunity.com/app/${appData.steamAppId}/",
            "https://steamcommunity.com/app/${appData.steamAppId}/",
        ))
        appData.commonDetails?.reviews?.onEachIndexed { i, it ->
            if (i == 0) return@onEachIndexed
            add(Triple(
                it.name,
                it.url.removePrefix("https://"),
                it.url,
            ))
        }
    }
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        links.forEach {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable(role = Role.Button) { OpenWebPage(it.third, context) }
            ) {
                Text(
                    text = it.first,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = it.second,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExternalLinksPreview() {
    NerdSteamTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
            ExternalLinksTab(appData = AppData(steamAppId = 220))
        }
    }
}
