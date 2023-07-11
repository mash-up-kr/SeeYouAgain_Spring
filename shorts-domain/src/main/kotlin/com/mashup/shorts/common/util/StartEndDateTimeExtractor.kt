package com.mashup.shorts.common.util

import java.time.LocalDateTime

object StartEndDateTimeExtractor {
    fun extractStarDateTimeAndEndDateTime(
        targetDateTime: LocalDateTime,
    ): Pair<LocalDateTime, LocalDateTime> {
        val startDateTime = targetDateTime
            .withHour(targetDateTime.hour)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        val endDateTime = targetDateTime
            .withHour(targetDateTime.hour)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)

        return Pair(startDateTime, endDateTime)
    }
}
