package com.github.khanshoaib3.nerdsteam.utils

import java.text.NumberFormat
import java.util.*

fun getNumberFormatFromCurrencyCode(currencyCode: String?): NumberFormat {
    if (currencyCode == null) return NumberFormat.getCurrencyInstance()
    val appLocale = Locale.getDefault()

    val currency = Currency.getInstance(currencyCode)

    if (currency == null) return NumberFormat.getCurrencyInstance(appLocale)

    val sameCurrencyLocales = Locale.getAvailableLocales().filter { locale ->
        runCatching { Currency.getInstance(locale) == currency }
            .getOrElse { false } // Some countries don't have a currency
    }
    if (sameCurrencyLocales.isEmpty()) return NumberFormat.getCurrencyInstance(appLocale)

    if (sameCurrencyLocales.size == 1) {
        return NumberFormat.getCurrencyInstance(sameCurrencyLocales.first())
    }

    val locale =
        sameCurrencyLocales.find { it.language == appLocale.language && it.country == appLocale.country }
            ?: sameCurrencyLocales.find { it.language == appLocale.language }
            ?: sameCurrencyLocales.find { it.country == appLocale.country }
            ?: sameCurrencyLocales.first()

    return NumberFormat.getCurrencyInstance(locale)
}
