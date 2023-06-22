package com.mashup.shorts.domain.news

import com.mashup.shorts.domain.newscard.Pivots

interface NewsQueryDSLRepository {

    fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        cursorWrittenDateTime: String,
        newsCardMultipleNews: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News>
}
