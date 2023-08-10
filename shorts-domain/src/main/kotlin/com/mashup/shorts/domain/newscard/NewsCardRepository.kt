package com.mashup.shorts.domain.newscard

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface NewsCardRepository : JpaRepository<NewsCard, Long>, NewsCardQueryDSLRepository {
    fun findAllByCreatedAtBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<NewsCard>
}
