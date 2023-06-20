package com.mashup.shorts.domain.newscard

import com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponseMapper

interface NewsCardQueryDSLRepository {

    fun findNewsCardsByMemberFilteredNewsIdsAndCursorId(
        filteredNewsIds: List<Long>,
        cursorId: Long,
        categories: List<Long>,
        size: Int,
    ): List<RetrieveAllNewsCardResponseMapper>

    fun findByKeywordsLikeAndCursorId(
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<NewsCard>

}
