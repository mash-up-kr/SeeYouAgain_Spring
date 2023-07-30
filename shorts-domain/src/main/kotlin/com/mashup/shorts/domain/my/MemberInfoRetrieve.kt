package com.mashup.shorts.domain.my

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRepository
import com.mashup.shorts.domain.membershortscount.MemberShortsCountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

@Service
@Transactional(readOnly = true)
class MemberInfoRetrieve(
    private val memberShortsCountRepository: MemberShortsCountRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository
) {

    fun retrieveMemberInfo(member: Member, now: LocalDate): MemberInfo {
        return MemberInfo(
            nickname = member.nickname,
            joinPeriod = (ChronoUnit.DAYS.between(LocalDate.from(member.createdAt), now) + 1).toInt(),
            totalShortsThisMonth = getTotalShortsThisMonth(now, member),
            todayShorts = memberNewsCardRepository.countAllByMember(member),
            savedShorts = memberNewsRepository.countAllByMember(member)
        )
    }

    fun getNumberOfReadsPerWeek(member: Member): Int {
        val currentWeek = extractWeeks()

        return memberShortsCountRepository.countByMemberAndTargetDateBetween(
            member = member,
            startTargetDate = currentWeek.first(),
            endTargetDate = currentWeek.last()
        )
    }

    private fun getTotalShortsThisMonth(now: LocalDate, member: Member): Int {
        val targetDateTime = LocalDate.of(now.year, now.month, 1).minusDays(1)
        var totalShorts = 0
        memberShortsCountRepository.findAllByMemberAndTargetDateAfter(member, targetDateTime)
            .map { msc -> totalShorts += msc.count }
        return totalShorts
    }

    private fun extractWeeks(): MutableList<LocalDate> {
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
