package com.mashup.shorts.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberNewsCardRepository : JpaRepository<MemberNewsCard, Long>
