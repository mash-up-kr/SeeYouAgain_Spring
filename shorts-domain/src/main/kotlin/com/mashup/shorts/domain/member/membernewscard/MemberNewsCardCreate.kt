package com.mashup.shorts.domain.member.membernewscard

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Transactional(readOnly = true)
@Service
class MemberNewsCardCreate(
    private val memberRepository: MemberRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository
) {

    @Transactional
    fun createMemberNewsCard(uniqueId: String, newsCardId: Long) {
        val member = memberRepository.findByUniqueId(uniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${uniqueId}에 해당하는 유저가 존재하지 않습니다."
        )
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${newsCardId}에 해당하는 뉴스카드가 존재하지 않습니다."
        )
        memberNewsCardRepository.save(MemberNewsCard(member = member, newsCard = newsCard))
    }
}
