package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.api.Platforms

private const val ITEM_NAME_WEIGHT = 0.35f
private const val ITEM_VALUE_WEIGHT = 0.65f

private data class OverviewItemScope(
    val mod: Modifier = Modifier,
)

@Composable
fun OverviewTable(
    appId: Int,
    imageUrl: String,
    appName: String,
    appType: String,
    developers: List<String>?,
    publishers: List<String>?,
    platforms: Platforms,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.Top, modifier = modifier) {
        Column(modifier = Modifier.weight(0.25f), verticalArrangement = Arrangement.Top) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Hero capsule for $appName",
                placeholder = painterResource(R.drawable.preview_image_300x450),
                modifier = Modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
            )
        }
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Column(modifier = Modifier.weight(0.75f)) {
            OverviewItem(
                name = "App ID",
                value = "$appId",
            )
            OverviewItem(
                name = "App Type",
                value = appType,
            )
            if (developers != null && developers.isNotEmpty()) {
                OverviewItem(
                    name = "Developer",
                    value = developers.joinToString(", "),
                )
            }
            if (publishers != null && publishers.isNotEmpty()) {
                OverviewItem(
                    name = "Publisher",
                    value = publishers.joinToString(", "),
                )
            }
            OverviewItem(
                first = "Platforms",
                second = {
                    Row(modifier = mod) {
                        if (platforms.windows) {
                            Icon(
                                painter = painterResource(R.drawable.windows_icon),
                                contentDescription = "Windows",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        if (platforms.linux) {
                            Icon(
                                painter = painterResource(R.drawable.linux_icon),
                                contentDescription = "Linux",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        if (platforms.mac) {
                            Icon(
                                painter = painterResource(R.drawable.mac_icon),
                                contentDescription = "MacOS",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun OverviewItem(
    first: String,
    second: @Composable OverviewItemScope.() -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = first,
            modifier = Modifier.weight(ITEM_NAME_WEIGHT),
            fontWeight = FontWeight.Bold,
        )
        second(OverviewItemScope(Modifier.weight(ITEM_VALUE_WEIGHT)))
    }
}

@Composable
private fun OverviewItem(
    name: String,
    value: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = name,
            modifier = Modifier.weight(ITEM_NAME_WEIGHT),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            modifier = Modifier
                .weight(ITEM_VALUE_WEIGHT)
                .horizontalScroll(rememberScrollState()),
            maxLines = 2,
        )
    }
}
