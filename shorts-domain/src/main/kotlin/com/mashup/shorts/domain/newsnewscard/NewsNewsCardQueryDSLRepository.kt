package com.mashup.shorts.domain.newsnewscard

import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newscard.Pivots

interface NewsNewsCardQueryDSLRepository {

    fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        cursorWrittenDateTime: String,
        newsCardMultipleNews: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News>
}
