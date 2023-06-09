package com.mashup.shorts.domain.newscard.dto

import com.mashup.shorts.domain.news.News

object NewsCardResponseForm {

    data class LoadAllDetailNewsInNewsCard(
        var id: Long,
        var title: String,
        var thumbnailImageUrl: String,
        var newsLink: String,
        var press: String,
        var writtenDateTime: String,
        var type: String,
    ) {
        companion object {
            fun persistenceToResponseForm(newsBundle: MutableList<News>)
                : MutableList<LoadAllDetailNewsInNewsCard> {
                val result = mutableListOf<LoadAllDetailNewsInNewsCard>()
                for (news in newsBundle) {
                    result.add(
                        LoadAllDetailNewsInNewsCard(
                            id = news.id,
                            title = news.title,
                            thumbnailImageUrl = news.thumbnailImageUrl,
                            newsLink = news.newsLink,
                            press = news.press,
                            writtenDateTime = news.writtenDateTime,
                            type = news.type
                        )
                    )
                }
                return result
            }
        }


    }

}
