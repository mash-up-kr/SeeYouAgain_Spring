package com.mashup.shorts.domain.my.membernewscard.dto

import java.time.LocalDateTime
import com.mashup.shorts.common.util.convert
import com.mashup.shorts.domain.membernewscard.dtomapper.MemberTodayShorts
import com.mashup.shorts.domain.newscard.NewsCard

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

data class RetrieveSavedNewsCardResponse(
    var numberOfShorts: Int,
    var numberOfReadShorts: Int,
    var memberShorts: List<MemberShorts>,
) {
    companion object {
        fun domainResponseFormToApiResponseForm(
            memberTodayShorts: MemberTodayShorts,
        ): RetrieveSavedNewsCardResponse {
            return RetrieveSavedNewsCardResponse(
                numberOfShorts = memberTodayShorts.numberOfShorts,
                numberOfReadShorts = memberTodayShorts.numberOfReadShorts,
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
