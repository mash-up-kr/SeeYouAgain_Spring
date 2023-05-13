package com.mashup.shorts.domain.news.image

import org.springframework.data.jpa.repository.JpaRepository

/**
 * NewsImageRepository
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
interface NewsImageRepository : JpaRepository<NewsImage, Long>
