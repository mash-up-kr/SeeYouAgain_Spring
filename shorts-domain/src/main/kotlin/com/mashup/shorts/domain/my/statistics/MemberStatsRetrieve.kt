package com.mashup.shorts.domain.my.statistics

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.util.CalendarUtil
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository

@Service
@Transactional(readOnly = true)
class MemberStatsRetrieve(
    private val memberNewsRepository: MemberNewsRepository
) {

    fun retrieveMemberWeeklyStats(
        member: Member,
        now: LocalDate,
    ): MemberWeeklyStats {
        val weeklyShortsCnt = LinkedHashMap<String, Int>()
        val categoryOfInterest = LinkedHashMap<String, Int>()
        val dateOfShortsRead = LinkedHashMap<String, MutableList<String>>()

        val shortsCntByDateList = getShortsCntByDate(member, now)
        setWeeklyShorts(now, shortsCntByDateList, weeklyShortsCnt, dateOfShortsRead)

        val shortsCntByCategoryList = getShortsCntByCategoryThisWeek(member, now)
        setCategoryOfInterest(now, shortsCntByCategoryList, weeklyShortsCnt, categoryOfInterest)

        return MemberWeeklyStats(
            weeklyShortsCnt = weeklyShortsCnt,
            categoryOfInterest = categoryOfInterest,
            dateOfShortsRead = dateOfShortsRead
        )
    }

    private fun setCategoryOfInterest(now: LocalDate, shortsCntByCategoryList: List<ShortsCntByCategory>,
                                      weeklyShortsCnt: LinkedHashMap<String, Int>,
                                      categoryOfInterest: LinkedHashMap<String, Int>) {
        categoryOfInterest["total"] = weeklyShortsCnt.getOrDefault(
            CalendarUtil.getCurrentWeekOfMonth(now.year, now.month.value, now.dayOfMonth), 0)
        for (shortsCntByCategory in shortsCntByCategoryList) {
            categoryOfInterest[shortsCntByCategory.category] = shortsCntByCategory.shortsCnt
        }
    }

    private fun getShortsCntByCategoryThisWeek(member: Member, now: LocalDate): List<ShortsCntByCategory> {
        val daysAfterMonday = now.dayOfWeek.value - DayOfWeek.MONDAY.value
        val startDate = now.minusDays(daysAfterMonday.toLong())
        return memberNewsRepository.getShortsCntByCategory(member.id, startDate.atStartOfDay()).stream()
            .map { vo -> ShortsCntByCategory(category = vo.getCategory(), shortsCnt = vo.getShortsCnt()) }.toList()
    }

    private fun setWeeklyShorts(
        now: LocalDate,
        shortsCntByDateList: List<ShortsCntByDate>,
        weeklyShortsCnt: LinkedHashMap<String, Int>,
        dateOfShortsRead: LinkedHashMap<String, MutableList<String>>,
    ) {
        dateOfShortsRead["lastWeek"] = mutableListOf()
        dateOfShortsRead["thisWeek"] = mutableListOf()

        var startIdx = 0

        val weekField = WeekFields.of(Locale.getDefault())

        for (week in (RESPONSE_WEEKS - 1) downTo 0) {
            val targetDateOfWeek = now.minusDays((week * 7).toLong())
            val targetDateOfNextWeek = targetDateOfWeek.plusDays(7)
            val daysAfterMonday = targetDateOfNextWeek.dayOfWeek.value - DayOfWeek.MONDAY.value
            val startDateOfNextWeek = targetDateOfNextWeek.minusDays(daysAfterMonday.toLong())

            var total = 0
            for (idx in startIdx until shortsCntByDateList.size) {
                if (shortsCntByDateList[idx].date >= startDateOfNextWeek) {
                    startIdx = idx
                    break
                }

                if (idx == shortsCntByDateList.size - 1) {
                    startIdx = idx + 1
                }

                if (week == 0 || week == 1) {
                    val weekName = if (week == 0) "thisWeek" else "lastWeek"
                    dateOfShortsRead[weekName]?.add(shortsCntByDateList[idx].date.toString())
                }

                total += shortsCntByDateList[idx].shortsCnt
            }

            val firstDayOfMonth = targetDateOfWeek.with(TemporalAdjusters.firstDayOfMonth())
            val firstWeekOfMonth = firstDayOfMonth.get(weekField.weekOfYear())
            val weekOfMonth = targetDateOfWeek.get(weekField.weekOfYear()) - firstWeekOfMonth
            weeklyShortsCnt["${targetDateOfWeek.year}년 ${targetDateOfWeek.monthValue}월 ${weekOfMonth}주차"] = total
        }
    }

    // 4주차(이번주 포함) 동안 날짜별 숏스 읽은 개수 조회
    private fun getShortsCntByDate(member: Member, now: LocalDate): List<ShortsCntByDate> {
        val minusDays = (RESPONSE_WEEKS - 1) * 7
        val targetDate = now.minusDays(minusDays.toLong())
        val daysAfterMonday = targetDate.dayOfWeek.value - DayOfWeek.MONDAY.value
        val startDate = targetDate.minusDays(daysAfterMonday.toLong())
        return memberNewsRepository.getShortsCntByDate(member.id, startDate.atStartOfDay()).stream()
            .map { vo -> ShortsCntByDate(date = vo.getDate(), shortsCnt = vo.getShortsCnt()) }.toList()
    }

    companion object {
        const val RESPONSE_WEEKS = 4
    }
}
