package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membershortscount.MemberShortsCount
import com.mashup.shorts.domain.membershortscount.MemberShortsCountRepository
import com.mashup.shorts.domain.my.MemberInfoRetrieve
import com.mashup.shorts.domain.newscard.NewsCardRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class MemberNewsCardDelete(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val newsCardRepository: NewsCardRepository,
    private val memberInfoRetrieve: MemberInfoRetrieve,
    private val memberShortsCountRepository: MemberShortsCountRepository,
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService,
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
        ) ?: MemberShortsCount(
            member = member,
            count = 0,
            targetDate = today
        )

        memberShortsCount.increaseCount()

        memberShortsCountRepository.save(memberShortsCount)
        memberLogBadgeFacadeService.readCompleteLog(member)
        memberLogBadgeFacadeService.memberNumberOfReadsPerWeekLog(
            member = member,
            count = memberInfoRetrieve.getNumberOfReadsPerWeek(member)
        )

        return mapOf("shortsCount" to memberShortsCount.count)
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
