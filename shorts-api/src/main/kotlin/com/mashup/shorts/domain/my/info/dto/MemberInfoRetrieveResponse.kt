package com.mashup.shorts.domain.my.info.dto

data class MemberInfoRetrieveResponse(
    val nickname: String,
    val joinPeriod: Int,
    val totalSavedShortCount: Int,
    val savedNewsCountByKeyword: Int,
    val savedNewsCountByNewsCard: Int
)
