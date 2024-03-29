package com.mashup.shorts.domain.home.membernewscard.dto

import com.mashup.shorts.util.convert
import com.mashup.shorts.domain.home.membernewscard.HomeTitleGenerator
import com.mashup.shorts.domain.member.ShowMode
import com.mashup.shorts.domain.newscard.NewsCard
import java.time.LocalDateTime

data class HomeResponse(
    var homeTitle: String,
    var newsCards: List<RetrieveHomeNewsCardResponse>
) {
    companion object {
        fun makeHomeResponse(showMode: ShowMode, newsCards: List<NewsCard>): HomeResponse {
            return HomeResponse(
                homeTitle = HomeTitleGenerator.generateRandomHomeTitle(showMode),
                newsCards = RetrieveHomeNewsCardResponse.newsCardToHomeResponseForm(newsCards)
            )
        }
    }
}

data class RetrieveHomeNewsCardResponse(
    var id: Long,
    var keywords: String,
    var category: String,
    var crawledDateTime: LocalDateTime,
) {


    companion object {
        fun newsCardToHomeResponseForm(
            newsCards: List<NewsCard>
        ): List<RetrieveHomeNewsCardResponse> {
            return newsCards.map {
                RetrieveHomeNewsCardResponse(
                    id = it.id,
                    keywords = it.keywords,
                    category = it.category.name.name,
                    crawledDateTime = convert(it.createdAt)
                )
            }
        }
    }
}
