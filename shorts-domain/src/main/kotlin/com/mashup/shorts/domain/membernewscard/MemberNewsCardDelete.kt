package com.mashup.shorts.domain.membernewscard

import java.time.LocalDate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
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

    fun clearMemberNewsCard(
        today: LocalDate,
        member: Member,
        newsCardId: Long,
    ): Map<String, Int> {
        memberNewsCardRepository.deleteByNewsCard(
            newsCardRepository.findByIdOrNull(newsCardId) ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "뉴스카드 다 읽었어요 처리 중 ${newsCardId}를 찾을 수 없습니다."
            )
        )

        val memberShortsCount = memberShortsCountRepository.findByMemberAndTargetDate(
            member = member,
            targetDate = today
        )

        memberShortsCount?.let {
            memberShortsCount.increaseCount()
            return mapOf("shortsCount" to memberShortsCount.count)
        }

        val newMemberShortsCount = MemberShortsCount(
            member = member,
            count = 1,
            targetDate = today
        )

        memberShortsCountRepository.save(newMemberShortsCount)
        return mapOf("shortsCount" to newMemberShortsCount.count)
    }

    fun bulkDeleteMemberNewsCard(member: Member, newsCardIds: List<Long>) {
        val newsCards = newsCardRepository.findAllById(newsCardIds)
        if (memberNewsCardRepository.findByNewsCardIn(newsCards).isEmpty()) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "저장되지 않은 숏스를 삭제할 수 없습니다."
            )
        }
        memberNewsCardRepository.deleteAllByMemberAndNewsCardIn(member, newsCards)
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun autoDeleteMemberNewsCard() {
        memberNewsCardRepository.deleteAll()
    }
}
