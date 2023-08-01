package com.mashup.shorts.domain.my

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.membernews.SavedFlag
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class MemberInfoRetrieve(
    private val memberNewsRepository: MemberNewsRepository,
) {

    fun retrieveMemberInfo(member: Member, now: LocalDate): MemberInfo {
        val countByKeyword = memberNewsRepository.countAllByMemberAndSavedFlag(
            member,
            SavedFlag.KEYWORD
        )

        val countByNewsCard = memberNewsRepository.countAllByMemberAndSavedFlag(
            member,
            SavedFlag.KEYWORD
        )

        return MemberInfo(
            nickname = member.nickname,
            joinPeriod = (ChronoUnit.DAYS.between(LocalDate.from(member.createdAt), now) + 1).toInt(),
            totalSavedShortCount = countByKeyword + countByNewsCard,
            savedNewsCountByKeyword = countByKeyword,
            savedNewsCountByNewsCard = countByNewsCard
        )
    }
}
