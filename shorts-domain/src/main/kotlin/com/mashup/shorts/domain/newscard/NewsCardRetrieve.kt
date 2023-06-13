package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newsnewscard.NewsNewsCardNativeQueryRepository

@Service
@Transactional(readOnly = true)
class NewsCardRetrieve(
    private val memberRepository: MemberRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val newsCardRepository: NewsCardRepository,
    private val newsNewsCardNativeQueryRepository: NewsNewsCardNativeQueryRepository,
) {

    fun retrieveNewsCardByMember(
        memberUniqueId: String,
        targetDateTime: LocalDateTime,
        cursorId: Long,
        size: Int,
    ): List<NewsCard> {
        val member = memberRepository.findByUniqueId(memberUniqueId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "${memberUniqueId}에 해당하는 사용자는 존재하지 않습니다."
            )

        val memberCategories = memberCategoryRepository.findByMember(member)

        if (memberCategories?.size == 0) {
            return newsCardRepository.findNewsCardsByTargetTimeAndAndMemberCategoryAndCursorId(
                targetDate = targetDateTime.toLocalDate(),
                targetHour = targetDateTime.hour,
                cursorId = cursorId,
                size = size
            )
        }

        return newsCardRepository.findNewsCardsByTargetTimeAndAndMemberCategoryAndCursorIdAndCategory(
            targetDate = targetDateTime.toLocalDate(),
            targetHour = targetDateTime.hour,
            cursorId = cursorId,
            size = size,
            categories = memberCategories!!.map { it.category.id }
        )
    }

    fun retrieveDetailNewsInNewsCard(
        newsCardId: Long,
        cursorId: Long,
        size: Int,
    ): MutableList<News> {
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "${newsCardId}에 해당 뉴스 카드는 존재하지 않습니다."
            )

        val newsIdBundle = newsCard.multipleNews.split(", ").map { it.toLong() }
        return newsNewsCardNativeQueryRepository.loadNewsBundleByCursorIdAndPersistenceNewsCardMultipleNews(
            cursorId,
            newsIdBundle,
            size
        )
    }
}
