package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime

interface NewsCardQueryDSLRepository {

    fun findNewsCardsByMemberFilteredNewsIds(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        categories: List<Long>,
    ): List<NewsCard>

    fun findNewsCardsByMemberFilteredNewsIdsAndCursorId(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        categories: List<Long>,
    ): List<NewsCard>

    fun findSavedNewsCardsByNewsCardIds(
        newsCardIds: List<Long>,
        cursorId: Long,
        size: Int,
    ): List<NewsCard>

    fun findByKeywordsLikeAndCursorId(
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<NewsCard>

}
