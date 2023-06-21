package com.mashup.shorts.domain.membernewscard

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Transactional(readOnly = true)
@Service
class MemberNewsCardCreate(
    private val newsCardRepository: NewsCardRepository,
    private val memberNewsCardRepository: MemberNewsCardRepository
) {

    @Transactional
    fun createMemberNewsCard(member: Member, newsCardId: Long) {
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${newsCardId}에 해당하는 뉴스카드가 존재하지 않습니다."
        )
        memberNewsCardRepository.save(MemberNewsCard(member = member, newsCard = newsCard))
    }
}
