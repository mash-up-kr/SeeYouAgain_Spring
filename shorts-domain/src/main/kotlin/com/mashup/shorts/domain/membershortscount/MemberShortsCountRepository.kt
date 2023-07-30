package com.mashup.shorts.domain.membershortscount

import com.mashup.shorts.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface MemberShortsCountRepository : JpaRepository<MemberShortsCount, Long> {
    fun findByMemberAndTargetDate(member: Member, targetDate: LocalDate): MemberShortsCount?

    fun findAllByMemberAndTargetDateAfter(member: Member, targetDate: LocalDate): List<MemberShortsCount>

    fun countByMemberAndTargetDateBetween(member: Member, startTargetDate: LocalDate, endTargetDate: LocalDate): Int

    fun findAllByMemberAndTargetDateBetween(member: Member, startDate: LocalDate, endDate: LocalDate): List<MemberShortsCount>
}
