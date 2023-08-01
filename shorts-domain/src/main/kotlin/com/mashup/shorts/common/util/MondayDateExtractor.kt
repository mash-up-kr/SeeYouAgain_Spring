package com.mashup.shorts.common.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object MondayDateExtractor {

    fun extractWeeks(): MutableList<LocalDate> {
        // 현재 날짜 기준으로 이번 주 월요일을 가져온다.
        val startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val currentWeek = mutableListOf<LocalDate>()
        var currentDate = startOfWeek

        repeat(7) {
            currentWeek.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        return currentWeek
    }
}