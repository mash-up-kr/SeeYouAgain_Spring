package com.mashup.shorts.domain.news

import java.time.LocalDateTime
import com.mashup.shorts.domain.newscard.Pivots

interface NewsQueryDSLRepository {

    fun loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        cursorWrittenDateTime: String,
        newsCardMultipleNews: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News>

    fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        cursorWrittenDateTime: String,
        newsCardMultipleNews: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News>


}
