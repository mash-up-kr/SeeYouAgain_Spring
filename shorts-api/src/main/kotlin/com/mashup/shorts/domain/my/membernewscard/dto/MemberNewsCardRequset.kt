package com.mashup.shorts.domain.my.membernewscard.dto

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
