package com.mashup.shorts.domain.news

import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.category.Category

@Repository
interface NewsRepository : JpaRepository<News, Long>, NewsQueryDSLRepository {

    fun findAllByCategory(category: Category): List<News>
    fun findTopByOrderByIdDesc(): News?

    fun findAllByCategoryAndCreatedAtBetween(
        category: Category,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): List<News>
}
