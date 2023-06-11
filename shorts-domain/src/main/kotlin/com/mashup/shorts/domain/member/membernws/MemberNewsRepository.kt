package com.mashup.shorts.domain.member.membernws

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long>
