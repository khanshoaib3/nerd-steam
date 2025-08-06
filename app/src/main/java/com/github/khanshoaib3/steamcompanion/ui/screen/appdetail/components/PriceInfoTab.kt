package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.core.content.ContextCompat.getSystemService
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.ITADPriceDealsInfo
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.ITADPriceInfo
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.AppData
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.AppViewState
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.DataType
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.Progress
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.utils.OpenWebPage
import com.github.khanshoaib3.steamcompanion.utils.getNumberFormatFromCurrencyCode
import java.text.NumberFormat
import java.util.Locale

val END_ROUNDED_CORNER_SHAPE = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
val START_ROUNDED_CORNER_SHAPE = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PriceInfoTab(
    appData: AppData,
    appViewState: AppViewState,
    fetchDataFromSourceCallback: (DataType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (appViewState.isThereAnyDealPriceInfoStatus) {
            Progress.NOT_QUEUED -> {
                LaunchedEffect(appViewState) {
                    fetchDataFromSourceCallback(DataType.IS_THERE_ANY_DEAL_PRICE_INFO)
                }
            }

            Progress.LOADING -> {
                LoadingIndicator()
            }

            Progress.FAILED -> {
                Icon(Icons.Default.ErrorOutline, "Error")
                Text("Unable to load!", style = MaterialTheme.typography.bodyMediumEmphasized)
            }

            Progress.LOADED -> appData.isThereAnyDealPriceInfo?.let { priceInfo ->
                OverviewRow(
                    currencyCode = priceInfo.currency,
                    threeMonthsLow = priceInfo.threeMonthsLow,
                    oneYearLow = priceInfo.oneYearLow,
                    historicLow = priceInfo.historicLow,
                )

                Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))

                Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))) {
                    Text(
                        text = "Deals",
                        style = MaterialTheme.typography.headlineMediumEmphasized,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    HorizontalDivider()
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                    ) {
                        priceInfo.deals.forEach {
                            DealCard(deal = it)
                        }
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))

                    Footer()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OverviewRow(
    currencyCode: String?,
    threeMonthsLow: Float?,
    oneYearLow: Float?,
    historicLow: Float?,
    modifier: Modifier = Modifier,
) {
    val numberFormat = getNumberFormatFromCurrencyCode(currencyCode)
    Card(
        modifier.fillMaxWidth(0.95f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryFixedDim)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small)),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CenterAlignedSelectableText(
                    text = if (threeMonthsLow == null) "-" else numberFormat.format(threeMonthsLow),
                    style = MaterialTheme.typography.headlineSmallEmphasized,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "3 Months Low",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CenterAlignedSelectableText(
                    text = if (oneYearLow == null) "-" else numberFormat.format(oneYearLow),
                    style = MaterialTheme.typography.headlineSmallEmphasized,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "1 Year Low",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CenterAlignedSelectableText(
                    text = if (historicLow == null) "-" else numberFormat.format(historicLow),
                    style = MaterialTheme.typography.headlineSmallEmphasized,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Historic Low",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DealCard(
    deal: ITADPriceDealsInfo,
    modifier: Modifier = Modifier,
) {
    val currencyFormatter = getNumberFormatFromCurrencyCode(deal.currency)
    val context = LocalContext.current
    val view = LocalView.current

    OutlinedCard(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shop and deal info
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = END_ROUNDED_CORNER_SHAPE,
                    )
                    .border(
                        border = BorderStroke(
                            Dp.Hairline,
                            MaterialTheme.colorScheme.outlineVariant
                        ),
                        shape = END_ROUNDED_CORNER_SHAPE,
                    )
                    .weight(1f)
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_large),
                        vertical = dimensionResource(R.dimen.padding_small),
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = deal.shopName,
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = "Regular Price: ${currencyFormatter.format(deal.regularPrice)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                if (deal.drms.isNotEmpty()) {
                    Text(
                        text = "DRM: ${deal.drms.fastJoinToString(", ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
                Row {
                    Text(
                        text = "Date: ${deal.timeStamp}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    if (deal.expiry != null) {
                        Text(
                            text = " (expires ${deal.expiry})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                }
            }

            // Price and discount percentage
            Column(
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = currencyFormatter.format(deal.price),
                    style = MaterialTheme.typography.bodyLargeEmphasized,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = NumberFormat.getPercentInstance(Locale.getDefault())
                        .format(-deal.discountPercentage / 100f),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            // Link and voucher button
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = START_ROUNDED_CORNER_SHAPE,
                    )
                    .border(
                        border = BorderStroke(
                            width = Dp.Hairline,
                            color = MaterialTheme.colorScheme.outlineVariant
                        ),
                        shape = START_ROUNDED_CORNER_SHAPE,
                    )
            ) {
                if (!deal.voucher.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                val clipboard: ClipboardManager = getSystemService(
                                    context,
                                    ClipboardManager::class.java
                                ) as ClipboardManager
                                val clip = ClipData.newPlainText("Voucher", deal.voucher)
                                clipboard.setPrimaryClip(clip)
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            }
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = START_ROUNDED_CORNER_SHAPE,
                            )
                            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Use Voucher:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = deal.voucher,
                                style = MaterialTheme.typography.bodyMediumEmphasized,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondary,
                            )
                            Spacer(Modifier.width(dimensionResource(R.dimen.padding_very_small)))
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copy voucher",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSecondary,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .clickable {
                            OpenWebPage(deal.url, context)
                        }
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = START_ROUNDED_CORNER_SHAPE,
                        )
                        .border(
                            border = BorderStroke(
                                width = Dp.Hairline,
                                color = MaterialTheme.colorScheme.outlineVariant
                            ),
                            shape = START_ROUNDED_CORNER_SHAPE,
                        )
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Open in browser",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
fun Footer(modifier: Modifier = Modifier, url: String = "https://isthereanydeal.com/") {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            // https://developer.android.com/reference/kotlin/androidx/compose/ui/text/LinkAnnotation
            buildAnnotatedString {
                append("powered by ")
                withLink(
                    link = LinkAnnotation.Url(
                        url = url,
                        styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                    )
                ) {
                    append("isthereanydeal.com")
                }
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
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
private fun PriceInfoTabPreview() {
    val appData = AppData(
        steamAppId = 220,
        isThereAnyDealPriceInfo = ITADPriceInfo(
            currency = "USD",
            historicLow = null,
            oneYearLow = 3.99f,
            threeMonthsLow = 4.99f,
            deals = listOf(
                ITADPriceDealsInfo(
                    shopName = "Steam",
                    url = "ee",
                    price = 4.75f,
                    regularPrice = 5.99f,
                    currency = "CAD",
                    discountPercentage = 25,
                    voucher = "ERODE",
                    timeStamp = "25 Jul 2025",
                    expiry = null,
                    drms = listOf(),
                ), ITADPriceDealsInfo(
                    shopName = "Steam",
                    url = "ee",
                    price = 2579f,
                    regularPrice = 2579f,
                    currency = "INR",
                    discountPercentage = 0,
                    voucher = null,
                    timeStamp = "25 Jul 2025",
                    expiry = null,
                    drms = listOf(
                        "Steam",
                        "Epic Store",
                        "Steam",
                        "Epic Store",
                    ),
                ), ITADPriceDealsInfo(
                    shopName = "Steam",
                    url = "ee",
                    price = 18000f,
                    regularPrice = 20000f,
                    currency = "JPY",
                    discountPercentage = 10,
                    voucher = null,
                    timeStamp = "25 Jul 2025",
                    expiry = "31 Jul 2025",
                    drms = listOf("Steam", "Ubisoft"),
                )
            )
        )
    )
    val appViewState = AppViewState(
        isThereAnyDealPriceInfoStatus = Progress.LOADED
    )
    SteamCompanionTheme {
        PriceInfoTab(
            appData = appData,
            appViewState = appViewState,
            fetchDataFromSourceCallback = {}
        )
    }
}
