package com.mashup.shorts.domain.membercompany

import org.springframework.data.jpa.repository.JpaRepository
import com.mashup.shorts.domain.member.Member

interface MemberCompanyRepository : JpaRepository<MemberCompany, Long> {

    fun findAllByMember(member: Member): List<MemberCompany>
}
