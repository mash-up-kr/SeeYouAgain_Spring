package com.mashup.shorts.domain.my

data class MemberInfo(
    val nickname: String,
    val joinPeriod: Int,
    val totalSavedShortCount: Int,
    val savedNewsCountByKeyword: Int,
    val savedNewsCountByNewsCard: Int,
)
