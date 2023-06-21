package com.mashup.shorts.domain.membershortscount

import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member

@Repository
interface MemberShortsCountRepository : JpaRepository<MemberShortsCount, Long> {
    fun findByMemberAndTargetTime(member: Member, targetTime: LocalDate): MemberShortsCount?

    fun findAllByMemberAndTargetTimeAfter(member: Member, targetTime: LocalDate): List<MemberShortsCount>
}
