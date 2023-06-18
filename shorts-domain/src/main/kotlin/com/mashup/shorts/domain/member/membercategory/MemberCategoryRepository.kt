package com.mashup.shorts.domain.member.membercategory

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member

@Repository
interface MemberCategoryRepository : JpaRepository<MemberCategory, Long> {
    fun findByMember(member: Member): List<MemberCategory>

    fun deleteAllByMember(member: Member)
}
