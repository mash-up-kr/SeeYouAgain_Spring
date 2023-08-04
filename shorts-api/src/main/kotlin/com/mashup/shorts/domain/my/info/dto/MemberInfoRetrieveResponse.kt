package com.mashup.shorts.domain.my.info.dto

data class MemberInfoRetrieveResponse(
    val nickname: String,
    val joinPeriod: Int,
    val totalSavedNewsCount: Int,
    val savedNewsCardCount: Int,
    val savedNewsCount: Int
)
