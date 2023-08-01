package com.mashup.shorts.domain.news

import java.time.LocalDateTime

interface NewsQueryDSLRepository {

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
        companies: List<String>,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<News>
}
