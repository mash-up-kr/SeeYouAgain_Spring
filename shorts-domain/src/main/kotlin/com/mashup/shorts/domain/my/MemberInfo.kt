package com.mashup.shorts.domain.my

data class MemberInfo(
    val nickname: String,
    val joinPeriod: Int,
    val totalShortsThisMonth: Int,
    val todayShorts: Int,
    val savedShorts: Int
)
