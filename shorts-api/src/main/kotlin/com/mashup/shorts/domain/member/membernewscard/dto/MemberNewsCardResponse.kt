package com.mashup.shorts.domain.member.membernewscard.dto

import java.time.LocalDateTime
import com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponseMapper

data class RetrieveAllNewsCardResponse(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            newsCards: List<RetrieveAllNewsCardResponseMapper>,
        ): List<RetrieveAllNewsCardResponse> {
            return newsCards.map {
                RetrieveAllNewsCardResponse(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category,
                    crawledDateTime = it.crawledDateTime,
                )
            }
        }
    }
}
