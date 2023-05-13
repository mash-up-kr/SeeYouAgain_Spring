package com.mashup.shorts.domain.member

import org.springframework.data.jpa.repository.JpaRepository

/**
 * MemberRepository
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
interface MemberRepository : JpaRepository<Member, Long>
