package com.mashup.shorts.domain.member.membernewscard

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional(readOnly = true)
class MemberNewsCardRetrieve(
    private val memberRepository: MemberRepository,
    private val memberCategoryRepository: MemberCategoryRepository,
    private val newsCardRepository: NewsCardRepository,
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

        if (memberCategories.isEmpty()) {
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
            categories = memberCategories.map { it.category.id }
        )
    }
}
