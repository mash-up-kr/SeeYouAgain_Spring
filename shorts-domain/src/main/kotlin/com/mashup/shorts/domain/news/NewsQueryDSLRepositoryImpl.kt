package com.mashup.shorts.domain.news

import java.time.LocalDateTime
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.news.QNews.news
import com.querydsl.jpa.impl.JPAQueryFactory

@Repository
class NewsQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NewsQueryDSLRepository {

    override fun loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        newsIds: List<Long>,
        size: Int,
    ): List<News> {
        return queryFactory
            .selectFrom(news)
            .where(news.id.`in`(newsIds))
            .where(news.createdAt.between(firstDayOfMonth, lastDayOfMonth))
            .limit(size.toLong())
            .fetch()
    }

    override fun loadNewsBundleByCursorIdAndTargetTime(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        cursorId: Long,
        newsIds: List<Long>,
        size: Int,
    ): List<News> {
        return queryFactory
            .selectFrom(news)
            .where(news.id.gt(cursorId))
            .where(news.id.`in`(newsIds))
            .where(news.createdAt.between(startDateTime, endDateTime))
            .limit(size.toLong())
            .fetch()
    }

    override fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        newsIds: List<Long>,
        size: Int,
    ): List<News> {
        return queryFactory
            .selectFrom(news)
            .where(news.id.`in`(newsIds))
            .limit(size.toLong())
            .fetch()
    }
}
