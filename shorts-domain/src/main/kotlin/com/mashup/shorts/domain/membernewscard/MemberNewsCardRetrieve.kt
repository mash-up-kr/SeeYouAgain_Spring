package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.common.util.StartEndDateTimeExtractor.extractStarDateTimeAndEndDateTime
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membercategory.MemberCategory
import com.mashup.shorts.domain.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
    ): List<NewsCard> {
        val memberCategories = memberCategoryRepository.findByMember(member)
        val filteredNewsIds = filterAlreadySavedNews(member)
        val filteredNewsCardIds = filterAlreadySavedNewsCards(member)
        val newsCards = extractExistNewsCards(
            targetDateTime = targetDateTime,
            memberCategories = memberCategories
        ).filter { it.multipleNews.isNotEmpty() }

        return newsCards.filter { newsCard ->
            !filteredNewsCardIds.contains(newsCard.id) &&
                    newsCard.multipleNews.split(", ")
                        .map { it.toLong() }
                        .intersect(filteredNewsIds.toSet())
                        .isEmpty()
        }.shuffled()
    }


    private fun filterAlreadySavedNewsCards(member: Member): List<Long> {
        return memberNewsRepository.findAllByMember(member).map { it.id }
    }

    private fun filterAlreadySavedNews(member: Member): List<Long> {
        return memberNewsRepository.findAllByMember(member).map { it.news.id }
    }

    private fun extractExistNewsCards(
        targetDateTime: LocalDateTime,
        memberCategories: List<MemberCategory>,
    ): List<NewsCard> {
        var (startDateTime, endDateTime) = extractStarDateTimeAndEndDateTime(targetDateTime)
        var newsCards = newsCardRepository.findNewsCardsByMemberFilteredNewsIds(
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            categories = memberCategories.map { it.category.id },
        )

        while (newsCards.isEmpty()) {
            startDateTime = startDateTime.minusHours(1)
            endDateTime = endDateTime.minusHours(1)
            newsCards = newsCardRepository.findNewsCardsByMemberFilteredNewsIds(
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                categories = memberCategories.map { it.category.id },
            )
        }

        return newsCards
    }
}