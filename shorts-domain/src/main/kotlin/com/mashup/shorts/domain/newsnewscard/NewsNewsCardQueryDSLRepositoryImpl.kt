package com.mashup.shorts.domain.newsnewscard

import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.QNews.Companion.news
import com.mashup.shorts.domain.newscard.QNewsCard.Companion.newsCard
import com.querydsl.jpa.impl.JPAQueryFactory

@Repository
class NewsNewsCardQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NewsNewsCardQueryDSLRepository {


    override fun findNewsByCursorIdAndNewsCardId(
        cursorNewsId: Long,
        newsCardId: Long,
        pageSize: Int,
    ): MutableList<News> {

        val persistenceNewsCard = queryFactory
            .selectFrom(newsCard)
            .where(newsCard.id.eq(newsCardId))
            .fetchOne()

        val persistenceNewsCardMultipleNews = persistenceNewsCard?.multipleNews
            ?.split(", ")
            ?.map { it.toLong() }

        return queryFactory
            .selectFrom(news)
            .where(
                news.id.gt(cursorNewsId),
                news.id.`in`(persistenceNewsCardMultipleNews)
            )
            .orderBy(news.id.asc())
            .limit(pageSize.toLong())
            .fetch()
    }
}
