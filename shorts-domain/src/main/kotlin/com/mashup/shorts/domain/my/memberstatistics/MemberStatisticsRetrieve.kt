package com.mashup.shorts.domain.my.memberstatistics

import com.mashup.shorts.common.util.StartEndDateTimeExtractor.extractStarDateMonthAndEndDateMonth
import com.mashup.shorts.domain.category.CategoryName
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

    fun retrieveMemberStatisticsByMemberAndTargetDate(
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

        val result = mutableMapOf(
            CategoryName.POLITICS.name to 0L,
            CategoryName.ECONOMIC.name to 0L,
            CategoryName.SOCIETY.name to 0L,
            CategoryName.CULTURE.name to 0L,
            CategoryName.WORLD.name to 0L,
            CategoryName.SCIENCE.name to 0L,
        )

        memberNewsCardStatistics.forEach { (category, count) ->
            if (count != 0L) {
                result[category] = count
            }
        }

        memberNewsStatistics.forEach { (category, count) ->
            if (count != 0L) {
                result[category] = result[category]!! + count
            }
        }

        return result
    }
}