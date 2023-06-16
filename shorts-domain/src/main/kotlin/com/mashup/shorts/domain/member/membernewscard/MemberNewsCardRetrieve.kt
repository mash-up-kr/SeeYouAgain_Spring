package com.mashup.shorts.domain.member.membernewscard

import java.time.LocalDateTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponse
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class MemberNewsCardRetrieve(
    private val memberRepository: MemberRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val memberCardNewsRepository: MemberNewsCardRepository,
    private val newsCardRepository: NewsCardRepository,
    private val newsRepository: NewsRepository,
) {

    fun retrieveNewsCardByMember(
        memberUniqueId: String,
        targetDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<RetrieveAllNewsCardResponse> {
        val member = memberRepository.findByUniqueId(memberUniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "${memberUniqueId}에 해당하는 사용자는 존재하지 않습니다."
        )

        val memberCategories = memberCategoryRepository.findByMember(member)

        if (memberCategories.isEmpty()) {
            return RetrieveAllNewsCardResponse.persistenceToResponseForm(
                newsCardRepository.findNewsCardsByTargetTimeAndAndMemberCategoryAndCursorId(
                    targetDate = targetDateTime.toLocalDate(),
                    targetHour = targetDateTime.hour,
                    cursorId = cursorId,
                    size = size
                )
            )
        }

        val filteredNewsIds = filterAlreadySavedNews(member)

        return RetrieveAllNewsCardResponse.persistenceToResponseForm(
            newsCardRepository.findNewsCardsByTargetTimeAndAndMemberCategoryAndCursorIdAndCategory(
                targetDate = targetDateTime.toLocalDate(),
                targetHour = targetDateTime.hour,
                filteredNewsIds = filteredNewsIds,
                cursorId = cursorId,
                size = size,
                categories = memberCategories.map { it.category.id }
            )
        )
    }

    private fun filterAlreadySavedNews(member: Member): List<Long> {
        val memberNewsCards = memberCardNewsRepository.findAllByMember(member)

        val result = mutableListOf<Long>()
        for (memberNewsCard in memberNewsCards) {
            val newsCard = memberNewsCard.newsCard
            val newsIds = newsCard.multipleNews.split(", ").map { it.toLong() }
            for (newsId in newsIds) {
                val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
                    shortsErrorCode = E404_NOT_FOUND,
                    resultErrorMessage = "${newsId}에 해당하는 뉴스는 존재하지 않습니다."
                )
                if (news.crawledCount > 1) {
                    result.add(news.id)
                }
            }
        }
        return result
    }
}
