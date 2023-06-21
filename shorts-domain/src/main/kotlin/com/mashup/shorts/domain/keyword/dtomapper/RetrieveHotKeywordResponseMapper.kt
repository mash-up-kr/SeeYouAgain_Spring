package com.mashup.shorts.domain.keyword.dtomapper

import java.time.LocalDateTime
import com.mashup.shorts.domain.newscard.NewsCard

data class RetrieveDetailHotKeyWordResponseMapper(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun persistenceToResponseForm(newsCards: List<NewsCard>): List<RetrieveDetailHotKeyWordResponseMapper> {
            return newsCards.map {
                RetrieveDetailHotKeyWordResponseMapper(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category.name.name,
                    crawledDateTime = it.createdAt,
                )
            }
        }
    }
}
