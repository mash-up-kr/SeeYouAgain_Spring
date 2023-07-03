package com.mashup.shorts.domain.membernewscard.dtomapper

import java.time.LocalDateTime
import com.mashup.shorts.domain.newscard.NewsCard
import com.querydsl.core.annotations.QueryProjection

data class RetrieveAllNewsCardResponseMapper @QueryProjection constructor(
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

data class MemberTodayShorts(
    var numberOfShorts: Int,
    var numberOfReadShorts: Int,
    var memberShorts: List<NewsCard>,
)
