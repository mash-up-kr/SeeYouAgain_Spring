package com.mashup.shorts.domain.membershortscount

import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member

@Repository
interface MemberShortsCountRepository : JpaRepository<MemberShortsCount, Long> {
    fun findByMemberAndTargetDate(member: Member, targetDate: LocalDate): MemberShortsCount?

    fun findAllByMemberAndTargetDateAfter(member: Member, targetDate: LocalDate): List<MemberShortsCount>

    fun findAllByMemberAndTargetDateBetween(member: Member, startDate: LocalDate, endDate: LocalDate): List<MemberShortsCount>
}
