package com.mashup.shorts.domain.membershortscount

import com.mashup.shorts.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface MemberShortsCountRepository : JpaRepository<MemberShortsCount, Long> {
    fun findAllByMemberAndTargetDateBetween(
        member: Member,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<MemberShortsCount>
}
