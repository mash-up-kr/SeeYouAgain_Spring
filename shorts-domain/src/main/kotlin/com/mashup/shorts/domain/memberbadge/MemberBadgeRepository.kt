package com.mashup.shorts.domain.memberbadge

import com.mashup.shorts.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberBadgeRepository : JpaRepository<MemberBadge, Long> {

    fun findByMember(member: Member): MemberBadge
}