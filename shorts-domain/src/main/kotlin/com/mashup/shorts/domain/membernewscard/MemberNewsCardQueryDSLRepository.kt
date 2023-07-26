package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernewscard.dtomapper.MemberStatisticsByNewsCard
import java.time.LocalDateTime

interface MemberNewsCardQueryDSLRepository {

    fun retrieveStatisticsByMemberAndTargetDate(
        member: Member,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): MutableList<MemberStatisticsByNewsCard>
}