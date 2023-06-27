package com.mashup.shorts.domain.my.membernewscard.dto

import com.mashup.shorts.domain.newscard.NewsCard
import java.time.LocalDateTime

data class RetrieveAllNewsCardResponse(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            newsCards: List<NewsCard>,
        ): List<RetrieveAllNewsCardResponse> {
            return newsCards.map {
                RetrieveAllNewsCardResponse(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category.name.name,
                    crawledDateTime = it.createdAt,
                )
            }
        }
    }
}

data class SavedRetrieveNewsCardByMember(
    var numberOfShorts: Int,
    var memberShorts : List<MemberShorts>
)

data class MemberShorts(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            newsCards: List<NewsCard>,
        ): List<MemberShorts> {
            return newsCards.map {
                MemberShorts(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category.name.name,
                    crawledDateTime = it.createdAt,
                )
            }
        }
    }
}
