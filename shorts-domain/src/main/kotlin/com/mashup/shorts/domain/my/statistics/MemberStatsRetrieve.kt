package com.mashup.shorts.domain.my.statistics

import java.time.DayOfWeek
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.util.CalendarUtil
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

    private fun setWeeklyShorts(now: LocalDate, shortsCntByDateList: List<ShortsCntByDate>,
                                weeklyShortsCnt: LinkedHashMap<String, Int>,
                                dateOfShortsRead: LinkedHashMap<String, MutableList<String>>) {
        dateOfShortsRead["lastWeek"] = mutableListOf()
        dateOfShortsRead["thisWeek"] = mutableListOf()

        var startIdx = 0
        for (week in (RESPONSE_WEEKS - 1) downTo 0) {
            // 해당 주차의 타깃 날짜
            val targetDateOfWeek = now.minusDays((week * 7).toLong())
            // 차주의 시작(월요일) 날짜 계산
            val targetDateOfNextWeek = targetDateOfWeek.plusDays(7)
            val daysAfterMonday = targetDateOfNextWeek.dayOfWeek.value - DayOfWeek.MONDAY.value
            val startDateOfNextWeek = targetDateOfNextWeek.minusDays(daysAfterMonday.toLong())

            var total = 0 // 해당 주차의 숏스 읽은 개수
            for (idx in startIdx until shortsCntByDateList.size) {
                if (shortsCntByDateList[idx].date >= startDateOfNextWeek) {
                    startIdx = idx
                    break
                }
                if (idx == shortsCntByDateList.size - 1) {
                    startIdx = idx + 1
                }
                // 이번주 & 지난주 숏스 읽은 날짜 추가
                if (week == 0 || week == 1) {
                    val weekName = if (week == 0) "thisWeek" else "lastWeek"
                    dateOfShortsRead[weekName]?.add(shortsCntByDateList[idx].date.toString())
                }
                total += shortsCntByDateList[idx].shortsCnt
            }
            // 해당 주차의 숏스 읽은 개수 업데이트
            weeklyShortsCnt[CalendarUtil.getCurrentWeekOfMonth(
                targetDateOfWeek.year, targetDateOfWeek.monthValue, targetDateOfWeek.dayOfMonth)] = total
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
