package com.mashup.shorts.domain.newscard

import com.mashup.shorts.domain.category.QCategory.category
import com.mashup.shorts.domain.newscard.QNewsCard.newsCard
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.LocalDateTime

@Repository
class NewsCardQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NewsCardQueryDSLRepository {

    override fun findNewsCardsByMemberCategory(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        categories: List<Long>,
    ): List<NewsCard> {
        return queryFactory
            .selectFrom(newsCard)
            .join(newsCard.category, category)
            .fetchJoin()
            .where(categoryCondition(categories))
            .where(newsCard.createdAt.between(startDateTime, endDateTime))
            .orderBy(newsCard.id.asc())
            .fetch()
    }

    override fun findSavedNewsCardsByNewsCardIds(
        newsCardIds: List<Long>,
        cursorId: Long,
        pivots: Pivots,
        size: Int
    ): List<NewsCard> {
        return queryFactory
            .selectFrom(newsCard)
            .where(cursorCondition(pivots, cursorId))
            .where(newsCard.id.`in`(newsCardIds))
            .orderBy(orderCondition(pivots))
            .limit(size.toLong())
            .fetch()
    }

    private fun categoryCondition(categories: List<Long>): Predicate? {
        return if (categories.isNotEmpty()) {
            newsCard.category.id.`in`(categories)
        } else {
            null
        }
    }

    private fun cursorCondition(pivots: Pivots, cursorId: Long): Predicate? {
        if (pivots == Pivots.ASC) {
            return if (cursorId != 0.toLong()) {
                newsCard.id.gt(cursorId)
            } else {
                return null
            }
        }
        return if (cursorId != 0.toLong()) {
            newsCard.id.lt(cursorId)
        } else {
            return null
        }
    }

    private fun orderCondition(pivots: Pivots): OrderSpecifier<*>? {
        if (pivots == Pivots.ASC) {
            return newsCard.id.asc()
        }
        return newsCard.id.desc()
    }
}
