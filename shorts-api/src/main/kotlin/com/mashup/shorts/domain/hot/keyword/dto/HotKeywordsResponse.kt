package com.mashup.shorts.domain.hot.keyword.dto

import java.time.LocalDateTime
import com.mashup.shorts.domain.keyword.dtomapper.RetrieveDetailHotKeyWordResponseMapper

data class HotKeywordsResponse(
    val createdAt: String,
    val ranking: List<String>,
) {

    companion object {
        fun of(createdAt: String, ranking: List<String>): HotKeywordsResponse {
            return HotKeywordsResponse(createdAt = createdAt, ranking = ranking)
        }
    }
}

data class RetrieveDetailHotKeywordResponse(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun mapperToResponseForm(
            retrieveDetailHotKeywordResponseMapper: List<RetrieveDetailHotKeyWordResponseMapper>,
        ): List<RetrieveDetailHotKeywordResponse> {
            return retrieveDetailHotKeywordResponseMapper.map {
                RetrieveDetailHotKeywordResponse(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category,
                    crawledDateTime = it.crawledDateTime,
                )
            }
        }
    }
}
