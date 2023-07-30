package com.mashup.shorts.common.util

import java.time.LocalDate
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class ShortsCalenderTest {

    @Test
    fun getWeekOfMonthTest() {
        val targetDate = LocalDate.now().minusDays(0)
        val weekOfMonth = ShortsCalender.getWeekOfMonth(targetDate)

        println(targetDate)
        println(weekOfMonth)
    }

}

