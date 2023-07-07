package com.mashup.shorts.domain.membernewscard

import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.util.StartEndDateTimeExtractor.extractStarDateTimeAndEndDateTime
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membercategory.MemberCategory
import com.mashup.shorts.domain.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.membernewscard.dtomapper.MemberTodayShorts
import com.mashup.shorts.domain.membershortscount.MemberShortsCountRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class MemberNewsCardRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val memberShortsCountRepository: MemberShortsCountRepository,
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
        )

        newsCards.map {
            if (it.multipleNews.isEmpty()) {
                throw ShortsBaseException.from(
                    shortsErrorCode = ShortsErrorCode.E500_INTERNAL_SERVER_ERROR,
                    resultErrorMessage = "뉴스를 불러오는 중 ${it.multipleNews} 가 비어있습니다."
                )
            }
        }

        return newsCards.filter { newsCard ->
            !filteredNewsCardIds.contains(newsCard.id) &&
                newsCard.multipleNews.split(", ")
                    .map { it.toLong() }
                    .intersect(filteredNewsIds.toSet())
                    .isEmpty()
        }.shuffled()
    }

    fun retrieveSavedNewsCardByMember(
        member: Member,
        cursorId: Long,
        size: Int,
    ): MemberTodayShorts {

        val memberShortsCount = memberShortsCountRepository.findByMemberAndTargetDate(
            member = member,
            targetDate = LocalDate.now()
        )

        if (memberShortsCount != null) {
            return MemberTodayShorts(
                numberOfShorts = memberNewsCardRepository.countAllByMember(member),
                numberOfReadShorts = memberShortsCount.count,
                memberShorts = newsCardRepository.findSavedNewsCardsByNewsCardIds(
                    newsCardIds = memberNewsCardRepository.findAllByMember(member).map { it.newsCard.id },
                    cursorId = cursorId,
                    size = size,
                )
            )
        }

        return MemberTodayShorts(
            numberOfShorts = memberNewsCardRepository.countAllByMember(member),
            numberOfReadShorts = 0,
            memberShorts = newsCardRepository.findSavedNewsCardsByNewsCardIds(
                newsCardIds = memberNewsCardRepository.findAllByMember(member).map { it.newsCard.id },
                cursorId = cursorId,
                size = size,
            )
        )
    }

    private fun filterAlreadySavedNewsCards(member: Member): List<Long> {
        return memberNewsCardRepository.findAllByMember(member).map { it.newsCard.id }
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
