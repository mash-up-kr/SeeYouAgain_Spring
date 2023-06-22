package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime

interface NewsCardQueryDSLRepository {

    fun findNewsCardsByMemberFilteredNewsIdsAndCursorId(
        filteredNewsIds: List<Long>,
        cursorId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        categories: List<Long>,
        size: Int,
    ): List<NewsCard>

    fun findByKeywordsLikeAndCursorId(
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<NewsCard>

}
