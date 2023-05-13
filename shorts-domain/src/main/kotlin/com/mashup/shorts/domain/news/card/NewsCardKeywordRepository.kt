package com.mashup.shorts.domain.news.card

import org.springframework.data.jpa.repository.JpaRepository

/**
 * NewsCardKeywordRepository
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
interface NewsCardKeywordRepository : JpaRepository<NewsCardKeyword, Long>
