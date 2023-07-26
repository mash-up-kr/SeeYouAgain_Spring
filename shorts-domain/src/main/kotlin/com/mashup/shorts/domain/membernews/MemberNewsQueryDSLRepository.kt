package com.mashup.shorts.domain.membernews

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.dtomapper.MemberStatisticsByNews
import com.querydsl.core.Tuple
import java.time.LocalDateTime

interface MemberNewsQueryDSLRepository {

    fun retrieveStatisticsByMemberAndTargetDate(
        member: Member,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): MutableList<MemberStatisticsByNews>
}