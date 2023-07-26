package com.mashup.shorts.domain.my.memberstatistics

import com.mashup.shorts.common.util.StartEndDateTimeExtractor.extractStarDateMonthAndEndDateMonth
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
@Transactional(readOnly = true)
class MemberStatisticsRetrieve(
    private val memberNewsCardRepository: MemberNewsCardRepository,
    private val memberNewsRepository: MemberNewsRepository,
) {

    fun retrieveMemberStatisticsByMemberCategoryTargetDate(
        member: Member,
        targetDateTime: LocalDateTime,
    ): MutableMap<String, Long> {

        val extractStarDateTimeAndEndDateTime = extractStarDateMonthAndEndDateMonth(targetDateTime)
        val memberNewsCardStatistics = memberNewsCardRepository.retrieveStatisticsByMemberAndTargetDate(
            member = member,
            startDateTime = extractStarDateTimeAndEndDateTime.first,
            endDateTime = extractStarDateTimeAndEndDateTime.second
        )
        val memberNewsStatistics = memberNewsRepository.retrieveStatisticsByMemberAndTargetDate(
            member = member,
            startDateTime = extractStarDateTimeAndEndDateTime.first,
            endDateTime = extractStarDateTimeAndEndDateTime.second
        )

        val result = mutableMapOf<String, Long>()

        memberNewsCardStatistics.forEach { (category, count) ->
            result[category] = count ?: 0L
        }

        memberNewsStatistics.forEach { (category, count) ->
            val currentCount = result[category] ?: 0L
            val updatedCount = count ?: 0L
            result[category] = currentCount + updatedCount
        }

        return result
    }
}