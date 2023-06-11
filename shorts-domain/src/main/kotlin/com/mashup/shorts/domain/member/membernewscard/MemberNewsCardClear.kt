package com.mashup.shorts.domain.member.membernewscard

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional
class MemberNewsCardClear(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val memberRepository: MemberRepository,
    private val newsCardRepository: NewsCardRepository,
) {

    fun clearMemberNewsCard(memberId: Long, newsCardId: Long) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_MEMBER_NOT_FOUND,
            resultErrorMessage = "${memberId}에 해당하는 유저가 존재하지 않습니다."
        )

        val newsCard = newsCardRepository.findByIdOrNull(newsCardId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NEWS_CARD_NOT_FOUND,
            resultErrorMessage = "${newsCardId}에 해당하는 뉴스카드가 존재하지 않습니다."
        )

        memberNewsCardRepository.deleteByMemberAndNewsCard(member, newsCard)
    }
}
