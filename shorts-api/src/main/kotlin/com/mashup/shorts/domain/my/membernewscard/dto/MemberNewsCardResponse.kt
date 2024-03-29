package com.mashup.shorts.domain.my.membernewscard.dto

import com.mashup.shorts.util.convert
import com.mashup.shorts.domain.newscard.NewsCard
import java.time.LocalDateTime

data class MemberNewsCardRetrieveResponse(
    var numberOfNewsCard: Int,
    var memberShorts: List<Shorts>,
) {
    companion object {
        fun newsCardsToResponseForm(
            newsCard: List<NewsCard>,
            allMemberNewsCardCount: Int,
        ): MemberNewsCardRetrieveResponse {
            return MemberNewsCardRetrieveResponse(
                numberOfNewsCard = allMemberNewsCardCount,
                memberShorts = newsCard.map {
                    Shorts(
                        id = it.id,
                        keywords = it.keywords,
                        category = it.category.name.name,
                        crawledDateTime = convert(it.createdAt),
                    )
                }
            )
        }
    }
}

data class Shorts(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
)
