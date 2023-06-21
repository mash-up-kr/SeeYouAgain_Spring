package com.mashup.shorts.domain.newsnewscard

import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.QNews.news
import com.mashup.shorts.domain.newscard.Pivots
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory


@Repository
class NewsNewsCardQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NewsNewsCardQueryDSLRepository {

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
            .orderBy(cursorOrderSpecifyByPivot(pivot))
            .limit(size.toLong())
            .fetch()
    }

    private fun cursorWrittenDateTimeLessThanGraterThanSpecifyByPivot(
        cursorWrittenDateTime: String,
        pivot: Pivots,
        newsCardMultipleNews: List<Long>,
    ): BooleanExpression? {
        if (cursorWrittenDateTime == "") {
            return news.id.`in`(newsCardMultipleNews)
        }

        return if (pivot == Pivots.ASC) {
            news.writtenDateTime.gt(cursorWrittenDateTime)
                .and(news.id.`in`(newsCardMultipleNews))
        } else
            news.writtenDateTime.lt(cursorWrittenDateTime)
                .and(news.id.`in`(newsCardMultipleNews))
    }

    private fun writtenDateTimeOrderSpecifyByPivot(pivot: Pivots): OrderSpecifier<String> {
        return if (pivot == Pivots.ASC) news.writtenDateTime.asc() else news.writtenDateTime.desc()
    }

    private fun cursorOrderSpecifyByPivot(pivot: Pivots): OrderSpecifier<Long> {
        return if (pivot == Pivots.ASC) news.id.asc() else news.id.desc()
    }
}
