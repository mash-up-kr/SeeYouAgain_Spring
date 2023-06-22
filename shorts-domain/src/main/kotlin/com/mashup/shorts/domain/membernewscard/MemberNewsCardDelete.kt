package com.mashup.shorts.domain.membernewscard

import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membershortscount.MemberShortsCount
import com.mashup.shorts.domain.membershortscount.MemberShortsCountRepository
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Service
@Transactional
class MemberNewsCardDelete(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberShortsCountRepository: MemberShortsCountRepository,
) {

    fun clearMemberNewsCard(today: LocalDate, member: Member): Map<String, Int> {
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
