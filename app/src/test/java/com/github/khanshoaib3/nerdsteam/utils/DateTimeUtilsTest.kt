package com.github.khanshoaib3.nerdsteam.utils

import com.github.marlonlom.utilities.timeago.TimeAgo
import org.junit.Assert
import org.junit.Test
import java.time.Instant
import java.time.OffsetDateTime
import kotlin.time.ExperimentalTime

class DateTimeUtilsTest {

    @Test
    fun `Test time conversion`() {
        val rawTime = "2025-08-01T19:01:34Z"
        val ee = Instant.parse(rawTime).toEpochMilli()
        println(TimeAgo.using(ee))
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `Test time conversion 2`() {
        val rawTimeOffset = "2025-07-24T23:15:17+02:30"
        val rawTime = "2025-07-24T20:15:17"
        println(OffsetDateTime.now())
        Assert.assertEquals("24 Jul 2025", DateTimeUtils.getConciseDate(rawTime))
        Assert.assertEquals("Jul 2025", DateTimeUtils.getConciseDate(rawTime, includeDay = false))
        Assert.assertEquals("25 Jul 2025", DateTimeUtils.getConciseDate(rawTimeOffset))
    }
}