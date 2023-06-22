package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.category.QCategory.category
import com.mashup.shorts.domain.newscard.QNewsCard.newsCard
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory

@Repository
class NewsCardQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NewsCardQueryDSLRepository {
    override fun findNewsCardsByMemberFilteredNewsIdsAndCursorId(
        filteredNewsIds: List<Long>,
        cursorId: Long,
        categories: List<Long>,
        size: Int,
    ): List<NewsCard> {
        return queryFactory
            .selectFrom(newsCard)
            .join(newsCard.category, category)
            .fetchJoin()
            .where(cursorCondition(cursorId))
            .where(categoryCondition(categories))
            .where(newsCard.createdAt.loe(LocalDateTime.now()))
            .orderBy(newsCard.id.desc())
            .limit(size.toLong())
            .fetch()
    }

    // 애플리케이션에서 필터링 하는 방법 고려해볼 것
    // 와일드카드를 맨 앞에 붙이면 인덱스를 타지 못하는 이유?
    override fun findByKeywordsLikeAndCursorId(
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<NewsCard> {
        return queryFactory
            .selectFrom(newsCard)
            .where(newsCard.id.gt(cursorId))
            .where(newsCard.keywords.like("%$keyword%"))
            .orderBy(newsCard.id.asc())
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

    private fun cursorCondition(cursorId: Long): Predicate? {
        return if (cursorId != 0.toLong()) {
            newsCard.id.lt(cursorId)
        } else {
            null
        }
    }
}
