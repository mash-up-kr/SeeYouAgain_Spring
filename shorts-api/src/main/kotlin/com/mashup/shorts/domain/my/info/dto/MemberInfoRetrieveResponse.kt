package com.mashup.shorts.domain.my.info.dto

data class MemberInfoRetrieveResponse(
    val nickname: String,
    val joinPeriod: Int,
    val totalShortsThisMonth: Int,
    val todayShorts: Int,
    val savedShorts: Int
)
