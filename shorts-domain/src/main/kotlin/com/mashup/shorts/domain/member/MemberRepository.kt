package com.mashup.shorts.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun existsByUniqueId(uniqueId: String): Boolean
    fun findByUniqueId(uniqueId: String): Member?
}
