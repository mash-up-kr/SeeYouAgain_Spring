package com.mashup.shorts.domain.newscard.dto

import com.mashup.shorts.domain.news.News
import io.swagger.v3.oas.annotations.media.Schema

data class NewsCardFormResponse(
    var id: Long,
    var title: String,
    var thumbnailImageUrl: String,
    var newsLink: String,
    var press: String,
    var writtenDateTime: String,
    var type: String,
) {
    companion object {
        fun persistenceToResponseForm(
            newsBundle: MutableList<News>,
        ): List<NewsCardFormResponse> {
            return newsBundle.map {
                NewsCardFormResponse(
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
