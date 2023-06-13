package com.mashup.shorts.domain.member.membernewscard.dto

import io.swagger.v3.oas.annotations.media.Schema

data class MemberNewsCardClearRequest(

    @Schema(description = "멤버 Id", example = "1")
    val memberId: Long,

    @Schema(description = "뉴스카드 Id", example = "1")
    val newsCardId: Long,
)
