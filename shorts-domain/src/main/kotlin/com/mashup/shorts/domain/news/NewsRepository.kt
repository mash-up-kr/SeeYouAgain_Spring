package com.mashup.shorts.domain.news

import com.mashup.shorts.domain.category.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface NewsRepository : JpaRepository<News, Long>, NewsQueryDSLRepository {

    fun findTopByOrderByIdDesc(): News?

    fun findAllByCategoryAndCreatedAtBetween(
        category: Category,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): List<News>
}
