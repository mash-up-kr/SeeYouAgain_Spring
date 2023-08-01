package com.mashup.shorts.domain.home.membernewscard.dto

import com.mashup.shorts.common.util.convert
import com.mashup.shorts.domain.newscard.NewsCard
import java.time.LocalDateTime

data class RetrieveHomeNewsCardResponse(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            newsCards: List<NewsCard>,
        ): List<RetrieveHomeNewsCardResponse> {
            return newsCards.map {
                RetrieveHomeNewsCardResponse(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category.name.name,
                    crawledDateTime = convert(it.createdAt),
                )
            }
        }
    }
}