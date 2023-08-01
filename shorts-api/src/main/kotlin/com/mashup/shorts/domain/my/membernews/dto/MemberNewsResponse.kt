package com.mashup.shorts.domain.my.membernews.dto

import com.mashup.shorts.domain.news.News

data class MemberNewsRetrieveResponse(
    var savedNewsCount: Int = 0,
    var memberNewsResponse: List<MemberNewsResponse>,
)

data class MemberNewsRetrieveByCompanyResponse(
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
    var category: String,
) {
    companion object {
        fun makeMemberNewsResponse(newsBundle: List<News>): List<MemberNewsResponse> {
            return newsBundle.map {
                MemberNewsResponse(
                    id = it.id,
                    title = it.title,
                    thumbnailImageUrl = it.thumbnailImageUrl,
                    newsLink = it.newsLink,
                    press = it.press,
                    writtenDateTime = it.writtenDateTime,
                    type = it.type,
                    category = it.category.name.name
                )
            }
        }
    }
}
