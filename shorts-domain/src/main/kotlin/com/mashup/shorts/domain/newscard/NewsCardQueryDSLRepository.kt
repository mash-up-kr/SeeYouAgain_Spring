package com.mashup.shorts.domain.newscard

interface NewsCardQueryDSLRepository {

    fun findNewsCardsByMemberFilteredNewsIdsAndCursorId(
        filteredNewsIds: List<Long>,
        cursorId: Long,
        categories: List<Long>,
        size: Int,
    ): List<NewsCard>

    fun findByKeywordsLikeAndCursorId(
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<NewsCard>

}
