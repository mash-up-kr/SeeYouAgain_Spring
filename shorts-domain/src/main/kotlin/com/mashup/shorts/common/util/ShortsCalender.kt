package com.mashup.shorts.common.util

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object ShortsCalender {

    private val calendar = Calendar.getInstance(Locale.KOREA)

    // 월의 몇주차인지 계산
    fun getWeekOfMonth(date: LocalDate): Int {
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.minimalDaysInFirstWeek = 7
        calendar.time = localDateToDate(date)
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }

    private fun localDateToDate(localDate: LocalDate): Date {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}
