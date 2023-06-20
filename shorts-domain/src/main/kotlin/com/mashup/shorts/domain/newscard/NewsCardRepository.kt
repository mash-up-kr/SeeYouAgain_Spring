package com.mashup.shorts.domain.newscard

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NewsCardRepository : JpaRepository<NewsCard, Long>, NewsCardQueryDSLRepository {

    @Query(
        "SELECT * FROM news_card " +
            "WHERE id > :cursorId " +
            "AND multiple_news not in (:filteredNewsIds) " +
            "AND created_at < NOW() " +
            "ORDER BY id ASC " +
            "LIMIT :size",
        nativeQuery = true
    )
    fun retrieveNewsCardByMemberNoCategory(
        @Param("filteredNewsIds") filteredNewsIds: List<Long>,
        @Param("cursorId") cursorId: Long,
        @Param("size") size: Int,
    ): List<NewsCard>

    @Query(
        "SELECT * FROM news_card " +
            "JOIN category on category.id = news_card.category.id " +
            "WHERE news_card.id > :cursorId " +
            "AND news_card.multiple_news not in (:filteredNewsIds) " +
            "AND news_card.category in (:categories) " +
            "AND news_card.created_at < NOW() " +
            "ORDER BY news_card.id ASC " +
            "LIMIT :size",
        nativeQuery = true
    )
    fun retrieveNewsCardByMemberAndCategory(
        @Param("filteredNewsIds") filteredNewsIds: List<Long>,
        @Param("cursorId") cursorId: Long,
        @Param("categories") categories: List<Long>,
        @Param("size") size: Int,
    ): List<NewsCard>
}
