package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime

interface NewsCardQueryDSLRepository {

    fun findNewsCardsByMemberCategory(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        categories: List<Long>,
    ): List<NewsCard>

    fun findSavedNewsCardsByNewsCardIds(
        newsCardIds: List<Long>,
        cursorId: Long,
        size: Int,
    ): List<NewsCard>
}
