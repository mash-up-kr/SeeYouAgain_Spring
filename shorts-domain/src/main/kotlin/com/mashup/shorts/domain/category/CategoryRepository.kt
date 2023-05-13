package com.mashup.shorts.domain.category

import org.springframework.data.jpa.repository.JpaRepository

/**
 * CategoryRepository
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
interface CategoryRepository : JpaRepository<Category, Long>
