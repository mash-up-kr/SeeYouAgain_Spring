package com.mashup.shorts.domain.member.membernewscard.dto

import java.time.LocalDateTime

data class RetrieveAllNewsCardResponse(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            newsCards: List<com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponse>,
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
