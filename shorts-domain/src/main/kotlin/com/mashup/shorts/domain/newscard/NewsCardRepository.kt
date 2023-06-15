package com.mashup.shorts.domain.newscard

import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.category.Category

@Repository
interface NewsCardRepository : JpaRepository<NewsCard, Long> {

    @Query(
        "SELECT * FROM news_card " +
            "WHERE id > :cursorId " +
            "AND created_at BETWEEN :targetDate AND (:targetDate + INTERVAL + :targetHour HOUR) " +
            "ORDER BY id ASC " +
            "LIMIT :size", nativeQuery = true
    )
    fun findNewsCardsByTargetTimeAndAndMemberCategoryAndCursorId(
        @Param("targetDate") targetDate: LocalDate,
        @Param("targetHour") targetHour: Int,
        @Param("cursorId") cursorId: Long,
        @Param("size") size: Int,
    ): List<NewsCard>

    @Query(
        "SELECT * FROM news_card " +
            "WHERE id > :cursorId " +
            "AND created_at BETWEEN :targetDate AND (:targetDate + INTERVAL + :targetHour HOUR) " +
            "AND category_id in :categories " +
            "AND multiple_news not in :filteredNewsIds " +
            "ORDER BY id ASC " +
            "LIMIT :size", nativeQuery = true
    )
    fun findNewsCardsByTargetTimeAndAndMemberCategoryAndCursorIdAndCategory(
        @Param("targetDate") targetDate: LocalDate,
        @Param("targetHour") targetHour: Int,
        @Param("filteredNewsIds") filteredNewsIds: List<Long>,
        @Param("cursorId") cursorId: Long,
        @Param("size") size: Int,
        @Param("categories") categories: List<Long>,
    ): List<NewsCard>
}
