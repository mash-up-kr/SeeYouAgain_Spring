package com.mashup.shorts.domain.member.membernews

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long>
