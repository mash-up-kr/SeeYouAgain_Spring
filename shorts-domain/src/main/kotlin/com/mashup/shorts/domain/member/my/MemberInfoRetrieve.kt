package com.mashup.shorts.domain.member.my

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membernews.MemberNewsRepository
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardRepository
import com.mashup.shorts.domain.member.membershortscount.MemberShortsCountRepository

@Service
@Transactional(readOnly = true)
class MemberInfoRetrieve(
    private val memberRepository: MemberRepository,
    private val memberShortsCountRepository: MemberShortsCountRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository
) {

    fun retrieveMemberInfo(memberUniqueId: String, now: LocalDate): MemberInfo {
        val member = memberRepository.findByUniqueId(memberUniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${memberUniqueId}에 해당하는 유저가 존재하지 않습니다."
        )
        return MemberInfo(
            nickname = member.nickname,
            joinPeriod = (ChronoUnit.DAYS.between(LocalDate.from(member.createdAt), now) + 1).toInt(),
            totalShortsThisMonth = getTotalShortsThisMonth(now, member),
            todayShorts = memberNewsCardRepository.countAllByMember(member),
            savedShorts = memberNewsRepository.countAllMyMember(member)
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
