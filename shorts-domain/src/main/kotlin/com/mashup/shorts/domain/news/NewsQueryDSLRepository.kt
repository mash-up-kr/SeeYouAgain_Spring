package com.mashup.shorts.domain.news

import java.time.LocalDateTime
import com.mashup.shorts.domain.newscard.Pivots

interface NewsQueryDSLRepository {

    fun loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        cursorWrittenDateTime: String,
        newsIds: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News>

    fun loadNewsBundleByCursorIdAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        cursorId: Long,
        newsIds: List<Long>,
        size: Int,
    ): List<News>

    fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        cursorWrittenDateTime: String,
        newsIds: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News>
}
