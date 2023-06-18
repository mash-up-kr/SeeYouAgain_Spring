package com.mashup.shorts.domain.member.my.dto

data class MemberInfoRetrieveResponse(
    val nickname: String,
    val joinPeriod: Int,
    val totalShortsThisMonth: Int,
    val todayShorts: Int,
    val savedShorts: Int
)
