package com.github.khanshoaib3.nerdsteam.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

class DateTimeUtils {
    companion object {
        fun getConciseDate(timeStamp: String?, includeDay: Boolean = true): String? {
            if (timeStamp.isNullOrBlank()) return null

            val formatter = LocalDateTime.Format {
                if (includeDay) {
                    day(); char(' ')
                }
                monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); year()
            }
            val localDateTime: LocalDateTime = try {
                OffsetDateTime.parse(timeStamp).atZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime().toKotlinLocalDateTime()
            } catch (_: DateTimeParseException) {
                LocalDateTime.parse(timeStamp)
            }

            return formatter.format(localDateTime)
        }

        // Steam refreshes the data at 10am PST, so we use that as the start of the new day
        fun getSteamDay(): LocalDate {
            val nowUtc = Instant.now().atZone(ZoneOffset.UTC)
            val steamDayStartUtc = nowUtc.withHour(17).withMinute(0).withSecond(0).withNano(0)

            return if (nowUtc.isBefore(steamDayStartUtc)) {
                // Still in previous "Steam day"
                nowUtc.minusDays(1).toLocalDate().toKotlinLocalDate()
            } else {
                // Already in today's "Steam day"
                nowUtc.toLocalDate().toKotlinLocalDate()
            }
        }
    }
}