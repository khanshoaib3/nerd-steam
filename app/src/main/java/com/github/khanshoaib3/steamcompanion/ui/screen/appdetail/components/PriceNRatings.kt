package com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.components

import android.icu.text.NumberFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.github.khanshoaib3.steamcompanion.R
import java.util.Currency

@Composable
fun PriceAndRating(
    modifier: Modifier = Modifier,
    isFree: Boolean,
    currentPrice: Float,
    originalPrice: Float,
    currency: String,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (!isFree && originalPrice != 0f) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Row(modifier = Modifier.fillMaxWidth(0.4f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    ) {
                        val numberInstance = NumberFormat.getNumberInstance()
                        val currentPrice = numberInstance.format(currentPrice)
                        val originalPrice = numberInstance.format(originalPrice)
                        val currencySymbol = Currency.getInstance(currency).symbol
                        Text(
                            text = "$currencySymbol $currentPrice",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Original: $currencySymbol $originalPrice",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
