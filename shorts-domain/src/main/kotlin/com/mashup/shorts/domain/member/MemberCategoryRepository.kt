package com.mashup.shorts.domain.member

import org.springframework.data.jpa.repository.JpaRepository

/**
 * MemberCategoryRepository
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
interface MemberCategoryRepository : JpaRepository<MemberCategory, Long>