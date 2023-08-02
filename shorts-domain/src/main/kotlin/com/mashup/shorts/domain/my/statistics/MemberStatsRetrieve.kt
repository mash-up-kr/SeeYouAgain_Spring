package com.mashup.shorts.domain.my.statistics

import com.mashup.shorts.common.util.ShortsCalender
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membershortscount.MemberShortsCount
import com.mashup.shorts.domain.membershortscount.MemberShortsCountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class MemberStatsRetrieve(
    private val memberShortsCountRepository: MemberShortsCountRepository
) {

    fun retrieveMemberWeeklyStats(
        member: Member,
        now: LocalDate,
    ): MemberWeeklyStats {
        val weeklyShortsCnt = LinkedHashMap<String, Int>()
        val dateOfShortsRead = LinkedHashMap<String, List<String>>()

        for (week in (RESPONSE_WEEKS - 1) downTo 0) { // 이번주 포함 총 {weeks}주차 조회
            val targetDate = now.minusDays((week * 7).toLong())
            val memberShortsCounts = getMemberShortsCountsForWeekByDate(member, targetDate)
            // 주차별 숏스 읽은 개수 설정
            weeklyShortsCnt[getWeekOfMonth(targetDate)] = getTotalShortsCnt(memberShortsCounts)
            // 이번주 & 지난주 숏스 읽은 날짜 설정
            if (week == 0 || week == 1) {
                val weekName = if (week == 0) "thisWeek" else "lastWeek"
                dateOfShortsRead[weekName] = getDateOfShortsRead(memberShortsCounts)
            }
        }

        return MemberWeeklyStats(
            weeklyShortsCnt = weeklyShortsCnt,
            dateOfShortsRead = dateOfShortsRead
        )
    }

    private fun getDateOfShortsRead(memberShortsCounts: List<MemberShortsCount>): List<String> {
        return memberShortsCounts.stream().map { it.targetDate.toString() }.toList()
    }

    // 해당 날짜가 속한 주차가 월의 몇주차인지 반환
    private fun getWeekOfMonth(targetDate: LocalDate): String {
        val weekOfMonth = ShortsCalender.getWeekOfMonth(targetDate)
        return StringBuilder().append(targetDate.month.value).append("월 ").append(weekOfMonth).append("주차").toString()
    }

    private fun getMemberShortsCountsForWeekByDate(member: Member, now: LocalDate): List<MemberShortsCount> {
        // 해당 주차의 월요일 날짜 조회
        val daysAfterMonday = now.dayOfWeek.value - DayOfWeek.MONDAY.value
        val dateOfMonday = now.minusDays(daysAfterMonday.toLong())
        // 해당 주차(월-일)의 MemberShortsCount 조회
        return memberShortsCountRepository.findAllByMemberAndTargetDateBetween(
            member,
            dateOfMonday,
            dateOfMonday.plusDays(6)
        )
    }

    private fun getTotalShortsCnt(memberShortsCounts: List<MemberShortsCount>): Int {
        var total = 0
        for (memberShortsCount in memberShortsCounts) {
            total += memberShortsCount.count
        }
        return total
    }

    companion object {
        const val RESPONSE_WEEKS = 4
    }
}
