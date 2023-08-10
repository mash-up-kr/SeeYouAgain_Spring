package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.common.util.StartEndDateTimeExtractor.extractStarDateTimeAndEndDateTime
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.ShowMode
import com.mashup.shorts.domain.membercategory.MemberCategory
import com.mashup.shorts.domain.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.membercompany.MemberCompanyRepository
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository
import com.mashup.shorts.domain.newscard.Pivots
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class MemberNewsCardRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberCompanyRepository: MemberCompanyRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository,
) {

    fun retrieveHome(member: Member, targetDateTime: LocalDateTime): List<NewsCard> {
        return when (member.showMode) {
            ShowMode.NORMAL -> retrieveNewsCardByMember(member, targetDateTime)
            else -> retrieveNewsCardByMemberAndCompanies(member, targetDateTime)
        }
    }

    private fun retrieveNewsCardByMember(
        member: Member,
        targetDateTime: LocalDateTime,
    ): List<NewsCard> {
        val memberCategories = memberCategoryRepository.findByMember(member)
        val filteredNewsIds = filterAlreadySavedNews(member)
        val filteredNewsCardIds = filterAlreadySavedNewsCards(member)
        val newsCards = extractAllNewsCardsByCategory(
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

    private fun retrieveNewsCardByMemberAndCompanies(
        member: Member,
        targetDateTime: LocalDateTime,
    ): List<NewsCard> {
        val newsCards = extractAllNewsCardsByCompany(targetDateTime = targetDateTime).filter {
            it.multipleNews.isNotEmpty()
        }

        val filteredNewsIds = filterAlreadySavedNews(member)

        val filteredNewsCardIds = filterAlreadySavedNewsCards(member)

        val memberCompanies = memberCompanyRepository.findAllByMember(member)

        return newsCards.filter { newsCard ->
            !filteredNewsCardIds.contains(newsCard.id) &&
                    newsCard.multipleNews.split(", ")
                        .map { it.toLong() }
                        .intersect(filteredNewsIds.toSet())
                        .isEmpty()
                    && newsCard.keywords.contains(memberCompanies.map { it.company.name }.toString())
        }.shuffled()
    }

    fun retrieveSavedNewsCardByMember(
        member: Member,
        cursorId: Long,
        size: Int,
        pivot: Pivots
    ): List<NewsCard> {

        var memberShorts = newsCardRepository.findSavedNewsCardsByNewsCardIds(
            newsCardIds = memberNewsCardRepository.findAllByMember(member).map { it.newsCard.id },
            cursorId = cursorId,
            size = size,
        )
        when (pivot) {
            Pivots.ASC -> memberShorts = memberShorts.sortedBy { it.createdAt }
            Pivots.DESC -> memberShorts = memberShorts.sortedByDescending { it.createdAt }
        }
        return memberShorts
    }


    private fun filterAlreadySavedNewsCards(member: Member): List<Long> {
        return memberNewsCardRepository.findAllByMember(member).map { it.newsCard.id }
    }

    private fun filterAlreadySavedNews(member: Member): List<Long> {
        return memberNewsRepository.findAllByMember(member).map { it.news.id }
    }

    private fun extractAllNewsCardsByCategory(
        targetDateTime: LocalDateTime,
        memberCategories: List<MemberCategory>,
    ): List<NewsCard> {
        var (startDateTime, endDateTime) = extractStarDateTimeAndEndDateTime(targetDateTime)
        var newsCards = newsCardRepository.findNewsCardsByMemberCategory(
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            categories = memberCategories.map { it.category.id },
        )

        while (newsCards.isEmpty()) {
            startDateTime = startDateTime.minusHours(1)
            endDateTime = endDateTime.minusHours(1)
            newsCards = newsCardRepository.findNewsCardsByMemberCategory(
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                categories = memberCategories.map { it.category.id },
            )
        }

        return newsCards
    }

    private fun extractAllNewsCardsByCompany(
        targetDateTime: LocalDateTime
    ): List<NewsCard> {
        var (startDateTime, endDateTime) = extractStarDateTimeAndEndDateTime(targetDateTime)
        var newsCards = newsCardRepository.findAllByCreatedAtBetween(
            startDateTime = startDateTime,
            endDateTime = endDateTime,
        )

        while (newsCards.isEmpty()) {
            startDateTime = startDateTime.minusHours(1)
            endDateTime = endDateTime.minusHours(1)
            newsCards = newsCardRepository.findAllByCreatedAtBetween(
                startDateTime = startDateTime,
                endDateTime = endDateTime,
            )
        }

        return newsCards
    }
}