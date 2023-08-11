package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberNewsCardDelete(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val newsCardRepository: NewsCardRepository
) {

    fun bulkDeleteMemberNewsCard(member: Member, newsCardIds: List<Long>) {
        val newsCards = newsCardRepository.findAllById(newsCardIds)
        val memberNewsCardList = memberNewsCardRepository.findByNewsCardIn(newsCards)
        if (memberNewsCardRepository.findByNewsCardIn(newsCards).isEmpty()) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "저장되지 않은 뉴스카드를 삭제할 수 없습니다."
            )
        }
        memberNewsCardList.map { it.softDelete() }
        memberNewsCardRepository.deleteAllByMemberAndNewsCardIn(member, newsCards)
    }
}