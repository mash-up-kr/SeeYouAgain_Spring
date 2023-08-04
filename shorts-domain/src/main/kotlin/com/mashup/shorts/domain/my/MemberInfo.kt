package com.mashup.shorts.domain.my

data class MemberInfo(
    val nickname: String,
    val joinPeriod: Int,
    val totalSavedNewsCount: Int,
    val savedNewsCount: Int,
    val savedNewsCardCount: Int,
)
