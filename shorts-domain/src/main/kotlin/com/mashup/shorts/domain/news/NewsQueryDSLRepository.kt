package com.mashup.shorts.domain.news

import java.time.LocalDateTime

interface NewsQueryDSLRepository {

    fun loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        newsIds: List<Long>,
        size: Int,
    ): List<News>

    fun loadNewsBundleByCursorIdAndTargetTime(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        cursorId: Long,
        newsIds: List<Long>,
        size: Int,
    ): List<News>

    fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        newsIds: List<Long>,
        size: Int,
    ): List<News>
}
