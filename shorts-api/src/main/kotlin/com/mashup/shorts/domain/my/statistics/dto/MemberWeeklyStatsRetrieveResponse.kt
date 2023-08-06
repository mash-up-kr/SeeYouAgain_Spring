package com.mashup.shorts.domain.my.statistics.dto

data class MemberWeeklyStatsRetrieveResponse(
    val weeklyShortsCnt: Map<String, Int>,
    val categoryOfInterest: Map<String, Int>,
    val dateOfShortsRead: Map<String, List<String>>
)
