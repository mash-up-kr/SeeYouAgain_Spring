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

    fun loadNewsBundleByCursorAndKeyword(
        keyword: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<News>

    fun loadNewsBundleByCursorAndCompany(
        company: List<String>,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<News>
}
