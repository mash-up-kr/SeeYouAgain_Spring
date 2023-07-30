package com.mashup.shorts.domain.memberlog

import com.mashup.shorts.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberLogRepository : JpaRepository<MemberLog, Long> {

    fun findByMember(member: Member): MemberLog
}