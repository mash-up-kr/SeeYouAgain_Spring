package com.mashup.shorts.domain.newscard.dto

import java.time.LocalDateTime
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newscard.NewsCard

data class RetrieveAllNewsCardResponse(
    var id: Long,
    var keywords: String,
    var createdAt: LocalDateTime,
) {
    companion object {
        fun persistenceToResponseForm(newsCards: List<NewsCard>): List<RetrieveAllNewsCardResponse> {
            return newsCards.map {
                RetrieveAllNewsCardResponse(
                    id = it.id,
                    keywords = it.keywords,
                    createdAt = it.createdAt,
                )
            }
        }
    }
}

data class DetailNewsCardResponse(
    var id: Long,
    var title: String,
    var thumbnailImageUrl: String,
    var newsLink: String,
    var press: String,
    var writtenDateTime: String,
    var type: String,
) {
    companion object {
        fun persistenceToResponseForm(newsBundle: MutableList<News>): List<DetailNewsCardResponse> {
            return newsBundle.map {
                DetailNewsCardResponse(
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
