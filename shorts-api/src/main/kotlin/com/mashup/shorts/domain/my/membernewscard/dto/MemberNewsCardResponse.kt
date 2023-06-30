package com.mashup.shorts.domain.my.membernewscard.dto

import java.time.LocalDateTime
import com.mashup.shorts.common.util.convert
import com.mashup.shorts.domain.membernewscard.dtomapper.MemberTodayShorts
import com.mashup.shorts.domain.newscard.NewsCard

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
                    crawledDateTime = convert(it.createdAt),
                )
            }
        }
    }
}

data class SavedRetrieveNewsCardByMember(
    var numberOfShorts: Int,
    var memberShorts: List<MemberShorts>,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            memberTodayShorts: MemberTodayShorts,
        ): SavedRetrieveNewsCardByMember {
            return SavedRetrieveNewsCardByMember(
                numberOfShorts = memberTodayShorts.numberOfShorts,
                memberShorts = memberTodayShorts.memberShorts.map {
                    MemberShorts(
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

data class MemberShorts(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
)
