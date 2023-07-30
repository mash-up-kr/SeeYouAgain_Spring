package com.mashup.shorts.domain.my.statistics.dto

import com.mashup.shorts.domain.my.statistics.MemberWeeklyStats

class MemberWeeklyStatsMapper {

    companion object {
        fun memberWeeklyStatsToResponse(memberWeeklyStats: MemberWeeklyStats): MemberWeeklyStatsRetrieveResponse {
            return MemberWeeklyStatsRetrieveResponse(
                weeklyShortsCnt = memberWeeklyStats.weeklyShortsCnt,
                dateOfShortsRead = memberWeeklyStats.dateOfShortsRead
            )
        }
    }
}
