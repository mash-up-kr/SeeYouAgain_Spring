package com.mashup.shorts.domain.member.membernewscard

import java.time.LocalDate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membershortscount.MemberShortsCount
import com.mashup.shorts.domain.member.membershortscount.MemberShortsCountRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional
class MemberNewsCardDelete(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val memberRepository: MemberRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberShortsCountRepository: MemberShortsCountRepository,
) {

    fun clearMemberNewsCard(memberId: Long, newsCardId: Long): Map<String, Int> {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "${memberId}에 해당하는 유저가 존재하지 않습니다."
        )
        val today = LocalDate.now()
        val memberShortsCount = memberShortsCountRepository.findByMemberAndTargetTime(
            member = member,
            targetTime = today
        )

        memberShortsCount?.let {
            memberShortsCount.increaseCount()
            return mapOf("shortsCount" to memberShortsCount.count)
        }

        val newMemberShortsCount = MemberShortsCount(
            member = member,
            count = 1,
            targetTime = today
        )
        memberShortsCountRepository.save(newMemberShortsCount)
        return mapOf("shortsCount" to newMemberShortsCount.count)
    }

    fun bulkDeleteMemberNewsCard(member: Member, newsCardIds: List<Long>) {
        val newsCards = newsCardRepository.findAllById(newsCardIds)
        memberNewsCardRepository.deleteAllByMemberAndNewsCardIn(member, newsCards)
    }
}
