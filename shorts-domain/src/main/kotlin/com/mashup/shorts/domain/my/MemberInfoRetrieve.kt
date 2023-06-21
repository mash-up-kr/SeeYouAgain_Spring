package com.mashup.shorts.domain.my

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRepository
import com.mashup.shorts.domain.membershortscount.MemberShortsCountRepository

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

    private fun getTotalShortsThisMonth(now: LocalDate, member: Member): Int {
        val targetDateTime = LocalDate.of(now.year, now.month, 1).minusDays(1)
        var totalShorts = 0
        memberShortsCountRepository.findAllByMemberAndTargetTimeAfter(member, targetDateTime)
            .map { msc -> totalShorts += msc.count }
        return totalShorts
    }
}
