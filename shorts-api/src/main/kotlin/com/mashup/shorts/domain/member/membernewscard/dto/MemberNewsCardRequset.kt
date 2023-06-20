package com.mashup.shorts.domain.member.membernewscard.dto

data class MemberNewsCardCreateRequest(
    val newsCardId: Long,
)

data class MemberNewsCardClearRequest(
    val memberId: Long,
    val newsCardId: Long,
)

data class MemberNewsCardBulkDeleteRequest(
    val newsCardIds: List<Long>,
)
