package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCardRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberNewsCardCreate(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService
) {

    fun createMemberNewsCard(member: Member, newsCardId: Long) {
        saveMemberNewsCard(member, newsCardId)
        memberLogBadgeFacadeService.saveNewsCardLog(member = member)
    }

    private fun saveMemberNewsCard(member: Member, newsCardId: Long) {
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "뉴스카드를 저장하는 중 ${newsCardId}에 해당하는 뉴스카드를 찾을 수 없습니다."
        )

        if (memberNewsCardRepository.existsByMemberAndNewsCard(member, newsCard)) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E400_BAD_REQUEST,
                resultErrorMessage = "뉴스는 중복해서 저장할 수 없습니다."
            )
        }
        memberNewsCardRepository.save(MemberNewsCard(member = member, newsCard = newsCard))
    }
}