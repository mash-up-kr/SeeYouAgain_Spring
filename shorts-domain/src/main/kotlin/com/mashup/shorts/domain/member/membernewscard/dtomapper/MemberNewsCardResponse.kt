package com.mashup.shorts.domain.member.membernewscard.dtomapper

import java.time.LocalDateTime
import com.mashup.shorts.domain.newscard.NewsCard

data class RetrieveAllNewsCardResponseMapper(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun persistenceToResponseForm(newsCards: List<NewsCard>): List<RetrieveAllNewsCardResponseMapper> {
            return newsCards.map {
                RetrieveAllNewsCardResponseMapper(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category.name.name,
                    crawledDateTime = it.createdAt,
                )
            }
        }
    }
}