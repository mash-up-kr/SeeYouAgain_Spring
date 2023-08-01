package com.mashup.shorts.domain.my.membernews.dto

data class MemberNewsCreateRequest(
    val newsId: Long
)

data class MemberNewsClearRequest(
    val newsId: Long
)