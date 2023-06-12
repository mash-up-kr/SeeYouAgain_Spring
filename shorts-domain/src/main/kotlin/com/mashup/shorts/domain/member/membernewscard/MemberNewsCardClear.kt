package com.mashup.shorts.domain.member.membernewscard

import java.time.LocalDate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membershortscount.MemberShortsCount
import com.mashup.shorts.domain.member.membershortscount.MemberShortsCountRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional
class MemberNewsCardClear(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val memberRepository: MemberRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberShortsCountRepository: MemberShortsCountRepository,
) {

    fun clearMemberNewsCard(memberId: Long, newsCardId: Long): Int {
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
            return memberShortsCount.count
        }

        val newMemberShortsCount = MemberShortsCount(
            member = member,
            count = 1,
            targetTime = today
        )
        memberShortsCountRepository.save(newMemberShortsCount)
        return newMemberShortsCount.count
    }

    fun deleteMemberNewsCard(uniqueId: String, newsCardId: Long) {
        val member = memberRepository.findByUniqueId(uniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "${uniqueId}에 해당하는 유저가 존재하지 않습니다."
        )

        val newsCard = newsCardRepository.findByIdOrNull(newsCardId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "${newsCardId}에 해당하는 뉴스카드가 존재하지 않습니다."
        )

        memberNewsCardRepository.deleteByMemberAndNewsCard(member, newsCard)
    }
}
