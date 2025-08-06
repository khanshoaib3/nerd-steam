package com.github.khanshoaib3.steamcompanion.utils

import java.text.NumberFormat
import java.util.*

private val APP_LOCALE = Locale("en", "in")

fun getNumberFormatFromCurrencyCode(currencyCode: String?): NumberFormat {
    if (currencyCode == null) return NumberFormat.getCurrencyInstance()

    val currency = Currency.getInstance(currencyCode)

    if (currency == null) return NumberFormat.getCurrencyInstance(APP_LOCALE)

    val sameCurrencyLocales = Locale.getAvailableLocales().filter { locale ->
        runCatching { Currency.getInstance(locale) == currency }
            .getOrElse { false } // Some countries don't have a currency
    }
    if (sameCurrencyLocales.isEmpty()) return NumberFormat.getCurrencyInstance(APP_LOCALE)

    if (sameCurrencyLocales.size == 1) {
        return NumberFormat.getCurrencyInstance(sameCurrencyLocales.first())
    }

    val locale =
        sameCurrencyLocales.find { it.language == APP_LOCALE.language && it.country == APP_LOCALE.country }
            ?: sameCurrencyLocales.find { it.language == APP_LOCALE.language }
            ?: sameCurrencyLocales.find { it.country == APP_LOCALE.country }
            ?: sameCurrencyLocales.first()


    // TODO Check if found multiple, then prioritise the one set in the app i.e, device's locale

    return NumberFormat.getCurrencyInstance(locale)
}
