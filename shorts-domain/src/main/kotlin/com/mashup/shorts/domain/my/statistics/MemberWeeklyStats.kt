package com.mashup.shorts.domain.my.statistics

data class MemberWeeklyStats(
    val weeklyShortsCnt: Map<String, Int>,
    val dateOfShortsRead: Map<String, List<String>>
)
