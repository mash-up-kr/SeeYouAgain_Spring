package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCardRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.memberlogbadge.MemberLogBadgeService

@Service
@Transactional
class MemberNewsCardCreate(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberLogBadgeService: MemberLogBadgeService
) {

    fun createMemberNewsCard(member: Member, newsCardId: Long) {
        saveMemberNewsCard(member, newsCardId)
        memberLogBadgeService.saveNewsCardLog(member = member)
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
