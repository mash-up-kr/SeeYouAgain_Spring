package com.mashup.shorts.common.util

import java.util.Calendar
import java.util.Locale

/*
참고 : https://subji.github.io/posts/2021/01/06/javagetweeknumberinkr
 */

object CalendarUtil {

    /**
     * 파라미터로 전달 된 날짜의 1일의 주차 계산
     * 1일이 목요일(5) 보다 클 경우 첫째 주 이므로 0을 반환
     * 1일이 월 ~ 목 이외의 날짜 일 경우 -1 을 반환.
     * 1일이 목요일(5) 보다 작으면 첫째 주가 아니므로 1을 반환
     * @param year
     * @param month
     * @param day
     * @return
     */
    private fun subWeekNumberIsFirstDayAfterThursday(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.set(year, month - 1, day)
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.firstDayOfWeek = Calendar.MONDAY

        val weekOfDay = calendar[Calendar.DAY_OF_WEEK]

        return when {
            weekOfDay >= Calendar.MONDAY && weekOfDay <= Calendar.THURSDAY -> 0
            day == 1 && (weekOfDay < Calendar.MONDAY || weekOfDay > Calendar.TUESDAY) -> -1
            else -> 1
        }
    }

    /**
     * 해당 날짜가 마지막 주에 해당하고 마지막주의 마지막날짜가 목요일보다 작으면
     * 다음달로 넘겨야 한다 +1
     * 목요일보다 크거나 같을 경우 이번달로 결정한다 +0
     * @param year
     * @param month
     * @param day
     * @return
     */
    private fun addMonthIsLastDayBeforeThursday(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[year, month - 1] = day

        val currentWeekNumber = calendar[Calendar.WEEK_OF_MONTH]
        val maximumWeekNumber = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)

        return if (currentWeekNumber == maximumWeekNumber) {
            calendar[year, month - 1] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val maximumDayOfWeek = calendar[Calendar.DAY_OF_WEEK]

            if (maximumDayOfWeek < Calendar.THURSDAY && maximumDayOfWeek > Calendar.SUNDAY) {
                1
            } else {
                0
            }
        } else {
            0
        }
    }

    /**
     * 해당 날짜의 주차를 반환
     * @param year
     * @param month
     * @param day
     * @return
     */
    fun getCurrentWeekOfMonth(year: Int, month: Int, day: Int): String {
        var subtractFirstWeekNumber = subWeekNumberIsFirstDayAfterThursday(year, month, day)
        val subtractLastWeekNumber = addMonthIsLastDayBeforeThursday(year, month, day)

        // 마지막 주차에서 다음 달로 넘어갈 경우 다음달의 1일을 기준으로 정해준다.
        // 추가로 다음 달 첫째주는 목요일부터 시작하는 과반수의 일자를 포함하기 때문에 한주를 빼지 않는다.
        var days = day
        if (subtractLastWeekNumber > 0) {
            days = 1
            subtractFirstWeekNumber = 0
        }

        if (subtractFirstWeekNumber < 0) {
            val calendar = Calendar.getInstance(Locale.KOREA)
            calendar[year, month - 1] = days
            calendar.add(Calendar.DATE, -1)

            return getCurrentWeekOfMonth(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1,
                calendar[Calendar.DATE]
            )
        }

        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.minimalDaysInFirstWeek = 1
        calendar[year, month - (1 - subtractLastWeekNumber)] = days

        val dayOfWeekForFirstDayOfMonth = calendar[Calendar.WEEK_OF_MONTH] - subtractFirstWeekNumber

        return "${calendar[Calendar.YEAR]}년 ${calendar[Calendar.MONTH] + 1}월 ${dayOfWeekForFirstDayOfMonth}주차"
    }
}
