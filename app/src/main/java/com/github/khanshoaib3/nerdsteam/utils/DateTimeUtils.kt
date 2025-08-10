package com.github.khanshoaib3.nerdsteam.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
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

//        fun getTimeAgoText(timeStamp: String) : String {
//            return TimeAgo.using(playerStatistics.lastHourTime.toEpochMilli())
//        }
    }
}