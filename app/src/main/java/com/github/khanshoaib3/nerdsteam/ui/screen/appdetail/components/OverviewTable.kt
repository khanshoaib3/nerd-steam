package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.CommonAppDetails
import com.github.khanshoaib3.nerdsteam.ui.components.MonochromeAsyncImage

private const val ITEM_NAME_WEIGHT = 0.35f
private const val ITEM_VALUE_WEIGHT = 0.65f

private data class OverviewItemScope(
    val mod: Modifier = Modifier,
)

@Composable
fun OverviewTable(
    appId: Int,
    commonAppDetails: CommonAppDetails,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.Top, modifier = modifier) {
        Column(
            modifier = Modifier.weight(0.25f),
            verticalArrangement = Arrangement.Center,
        ) {
            MonochromeAsyncImage(
                model = commonAppDetails.imageUrl,
                contentDescription = "Hero icon for ${commonAppDetails.name}",
                modifier = Modifier.aspectRatio(2f / 3f) // 300 x 450
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
                value = commonAppDetails.type,
            )
            if (!commonAppDetails.developers.isNullOrEmpty()) {
                OverviewItem(
                    name = "Developer",
                    value = commonAppDetails.developers.joinToString(", "),
                )
            }
            if (!commonAppDetails.publishers.isNullOrEmpty()) {
                OverviewItem(
                    name = "Publisher",
                    value = commonAppDetails.publishers.joinToString(", "),
                )
            }
            OverviewItem(name = "Platforms") {
                Row(modifier = mod) {
                    if (commonAppDetails.platforms.windows) {
                        Icon(
                            painter = painterResource(R.drawable.brands_windows),
                            contentDescription = "Windows",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (commonAppDetails.platforms.linux) {
                        Icon(
                            painter = painterResource(R.drawable.brands_linux),
                            contentDescription = "Linux",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (commonAppDetails.platforms.mac) {
                        Icon(
                            painter = painterResource(R.drawable.brands_mac),
                            contentDescription = "MacOS",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            OverviewItem(
                name = "Release Date",
                value = commonAppDetails.releaseDate
            )
            if (!commonAppDetails.tags.isNullOrEmpty()) {
                OverviewItem(
                    name = "Tags",
                    value = commonAppDetails.tags.joinToString(", "),
                )
            }
            if (!commonAppDetails.websiteUrl.isNullOrBlank()) {
                OverviewItem(name = "Website") {
                    SelectionContainer(modifier = mod) {
                        Text(
                            text = buildAnnotatedString {
                                withLink(
                                    link = LinkAnnotation.Url(
                                        url = commonAppDetails.websiteUrl,
                                        styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                                    )
                                ) {
                                    append("Open in Browser")
                                }
                            }

                        )
                    }
                }
            }
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun OverviewItem(
    name: String,
    value: @Composable OverviewItemScope.() -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_very_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SelectionContainer(
            modifier = Modifier
                .weight(ITEM_NAME_WEIGHT)
                .horizontalScroll(rememberScrollState()),
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
            )
        }
        value(OverviewItemScope(Modifier.weight(ITEM_VALUE_WEIGHT)))
    }
}

@Composable
private fun OverviewItem(
    name: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SelectionContainer(
            modifier = Modifier
                .weight(ITEM_NAME_WEIGHT)
                .horizontalScroll(rememberScrollState()),
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
            )
        }
        SelectionContainer(
            modifier = Modifier
                .weight(ITEM_VALUE_WEIGHT)
                .horizontalScroll(rememberScrollState()),
        ) {
            Text(
                text = value,
                maxLines = 2,
            )
        }
    }
}
