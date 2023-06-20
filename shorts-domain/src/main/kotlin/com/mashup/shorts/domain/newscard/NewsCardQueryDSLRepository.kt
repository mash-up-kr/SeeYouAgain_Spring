package com.mashup.shorts.domain.newscard

import com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponseMapper

interface NewsCardQueryDSLRepository {

    fun findNewsCardsByMemberCategoryAndCursorId(
        filteredNewsIds: List<String>,
        cursorId: Long,
        size: Int,
        categories: List<Long>,
    ): List<RetrieveAllNewsCardResponseMapper>

}
