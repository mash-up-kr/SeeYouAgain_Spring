package com.mashup.shorts.domain.member.membernews

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long> {

    fun countAllMyMember(member: Member): Int
}
