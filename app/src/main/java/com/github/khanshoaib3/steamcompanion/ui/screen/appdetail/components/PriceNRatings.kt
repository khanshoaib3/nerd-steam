package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import android.icu.text.NumberFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.Reviews
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.utils.OpenWebPage
import com.github.khanshoaib3.steamcompanion.utils.getNumberFormatFromCurrencyCode
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PriceAndRating(
    modifier: Modifier = Modifier,
    isFree: Boolean,
    currentPrice: Float,
    originalPrice: Float,
    currency: String,
    review: Reviews?,
) {
    if (!isFree && originalPrice != 0f && review == null) return
    val context = LocalContext.current
    FlowRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        if (!isFree && originalPrice != 0f) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    val currencyFormatter = getNumberFormatFromCurrencyCode(currency)
                    Text(
                        text = currencyFormatter.format(currentPrice),
                        style = MaterialTheme.typography.headlineMediumEmphasized,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Original: ${currencyFormatter.format(originalPrice)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }
        }
        if (review != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .clickable {
                            OpenWebPage(review.url, context)
                        }
                ) {
                    Text(
                        text = NumberFormat.getNumberInstance(Locale.getDefault()).format(review.score),
                        style = MaterialTheme.typography.headlineMediumEmphasized,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        review.name,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun PriceAndRatingPreview() {
    SteamCompanionTheme {
        PriceAndRating(
            isFree = false,
            currentPrice = 1499f,
            originalPrice = 2999f,
            currency = "INR",
            review = Reviews(
                score = 96,
                name = "Metacritic User Score",
                url = "https://metacritic.com/game/europa-universalis-iv/user-reviews/?platform=pc",
                count = 1399,
            )
        )
    }
}
