package com.mashup.shorts.domain.home.newscard.dto

import com.mashup.shorts.domain.news.News

data class RetrieveNewsBundleInNewsCardResponse(
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
        fun persistenceToResponseForm(newsBundle: List<News>):
            List<RetrieveNewsBundleInNewsCardResponse>
        {
            return newsBundle.map {
                RetrieveNewsBundleInNewsCardResponse(
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
