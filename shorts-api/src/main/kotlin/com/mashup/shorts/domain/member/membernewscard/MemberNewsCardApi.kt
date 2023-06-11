package com.mashup.shorts.domain.member.membernewscard

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest

@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardApi(
    private val memberNewsCardClear: MemberNewsCardClear,
) {

    @DeleteMapping
    fun clearMemberNewsCard(
        @RequestBody memberNewsCardRequest: MemberNewsCardClearRequest,
    ): ApiResponse<Any> {
        memberNewsCardClear.clearMemberNewsCard(
            memberId = memberNewsCardRequest.memberId,
            newsCardId = memberNewsCardRequest.newsCardId,
        )

        return ApiResponse.Companion.success(OK)
    }
}
