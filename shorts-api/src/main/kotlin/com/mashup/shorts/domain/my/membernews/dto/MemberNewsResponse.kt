package com.mashup.shorts.domain.my.membernews.dto

import java.time.LocalDate
import com.mashup.shorts.domain.news.News

data class MemberNewsRetrieveResponse(
    val today: LocalDate? = LocalDate.now(),
    var savedShortsCount: Int = 0,
    var memberNewsResponse: List<MemberNewsResponse>,
)

data class MemberNewsResponse(
    var id: Long,
    var title: String,
    var thumbnailImageUrl: String,
    var newsLink: String,
    var press: String,
    var writtenDateTime: String,
    var type: String,
) {
    companion object {
        fun persistenceToResponseForm(newsBundle: List<News>): List<MemberNewsResponse> {
            return newsBundle.map {
                MemberNewsResponse(
                    id = it.id,
                    title = it.title,
                    thumbnailImageUrl = it.thumbnailImageUrl,
                    newsLink = it.newsLink,
                    press = it.press,
                    writtenDateTime = it.writtenDateTime,
                    type = it.type
                )
            }
        }
    }
}