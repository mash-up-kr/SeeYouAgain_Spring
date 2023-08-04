package com.mashup.shorts.domain.my

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class MemberInfoRetrieve(
    private val memberNewsRepository: MemberNewsRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository,
) {

    fun retrieveMemberInfo(member: Member, now: LocalDate): MemberInfo {
        val countMemberNews = memberNewsRepository.findAllByMember(member).count()
        val countMemberNewsCard = memberNewsCardRepository.findAllByMember(member).count()

        return MemberInfo(
            nickname = member.nickname,
            joinPeriod = (ChronoUnit.DAYS.between(LocalDate.from(member.createdAt), now) + 1).toInt(),
            totalSavedNewsCount = countMemberNews + countMemberNewsCard,
            savedNewsCount = countMemberNews,
            savedNewsCardCount = countMemberNewsCard
        )
    }
}
