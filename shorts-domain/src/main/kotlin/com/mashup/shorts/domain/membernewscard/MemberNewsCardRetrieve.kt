package com.mashup.shorts.domain.membernewscard

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class MemberNewsCardRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberNewsRepository: MemberNewsRepository,
) {

    fun retrieveNewsCardByMember(
        member: Member,
        targetDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<NewsCard> {
        val memberCategories = memberCategoryRepository.findByMember(member)
        val filteredNewsIds = filterAlreadySavedNews(member)

        val newsCards = newsCardRepository.findNewsCardsByMemberFilteredNewsIdsAndCursorId(
            filteredNewsIds = filteredNewsIds,
            cursorId = cursorId,
            categories = memberCategories.map { it.category.id },
            size = size
        )

        return newsCards.filter { it ->
            it.multipleNews.split(", ")
                .map { it.toLong() }
                .intersect(filteredNewsIds.toSet())
                .isEmpty()
        }
    }

    private fun filterAlreadySavedNews(member: Member): List<Long> {
        return memberNewsRepository.findAllByMember(member).map { it.news.id }
    }
}
