package com.mashup.shorts.domain.news

import java.time.LocalDateTime
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.news.QNews.news
import com.mashup.shorts.domain.newscard.Pivots
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory

@Repository
class NewsQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NewsQueryDSLRepository {

    override fun loadNewsBundleByCursorAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        cursorWrittenDateTime: String,
        newsIds: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        return queryFactory
            .selectFrom(news)
            .where(
                cursorWrittenDateTimeLessThanGraterThanSpecifyByPivot(
                    cursorWrittenDateTime,
                    pivot,
                    newsIds
                )
            ).where(
                news.createdAt.between(firstDayOfMonth, lastDayOfMonth)
            )
            .orderBy(writtenDateTimeOrderSpecifyByPivot(pivot))
            .limit(size.toLong())
            .fetch()
    }

    override fun loadNewsBundleByCursorIdAndNewsCardMultipleNewsAndTargetTime(
        firstDayOfMonth: LocalDateTime,
        lastDayOfMonth: LocalDateTime,
        cursorId: Long,
        newsIds: List<Long>,
        size: Int,
    ): List<News> {
        return queryFactory
            .selectFrom(news)
            .where(news.id.gt(cursorId))
            .where(news.createdAt.between(firstDayOfMonth, lastDayOfMonth))
            .orderBy(news.id.asc())
            .limit(size.toLong())
            .fetch()
    }

    override fun loadNewsBundleByCursorAndNewsCardMultipleNews(
        cursorWrittenDateTime: String,
        newsCardMultipleNews: List<Long>,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        return queryFactory
            .selectFrom(news)
            .where(
                cursorWrittenDateTimeLessThanGraterThanSpecifyByPivot(
                    cursorWrittenDateTime,
                    pivot,
                    newsCardMultipleNews
                )
            )
            .orderBy(writtenDateTimeOrderSpecifyByPivot(pivot))
            .limit(size.toLong())
            .fetch()
    }

    private fun cursorWrittenDateTimeLessThanGraterThanSpecifyByPivot(
        cursorWrittenDateTime: String,
        pivot: Pivots,
        newsIds: List<Long>,
    ): BooleanExpression? {
        if (cursorWrittenDateTime == "") {
            return news.id.`in`(newsIds)
        }

        return if (pivot == Pivots.ASC) {
            news.writtenDateTime.gt(cursorWrittenDateTime)
                .and(news.id.`in`(newsIds))
        } else
            news.writtenDateTime.lt(cursorWrittenDateTime)
                .and(news.id.`in`(newsIds))
    }

    private fun writtenDateTimeOrderSpecifyByPivot(pivot: Pivots): OrderSpecifier<String> {
        return if (pivot == Pivots.ASC) news.writtenDateTime.asc() else news.writtenDateTime.desc()
    }
}
