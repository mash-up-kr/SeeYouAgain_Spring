package com.mashup.shorts.domain.member.membernewscard

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardBulkDeleteRequest
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest

@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardDeleteApi(
    private val memberNewsCardDelete: MemberNewsCardDelete,
) {

    @Auth
    @PostMapping
    fun bulkDeleteMemberNewsCard(
        @RequestBody memberNewsCardBulkDeleteRequest: MemberNewsCardBulkDeleteRequest,
    ): ApiResponse<Void> {
        val memberUniqueId = AuthContext.getMemberId()
        memberNewsCardDelete.bulkDeleteMemberNewsCard(
            uniqueId = memberUniqueId,
            newsCardIds = memberNewsCardBulkDeleteRequest.newsCardIds
        )
        return success(OK)
    }

    @DeleteMapping
    fun clearMemberNewsCard(
        @RequestBody memberNewsCardRequest: MemberNewsCardClearRequest,
    ): ApiResponse<Map<String, Int>> {
        return success(
            OK,
            memberNewsCardDelete.clearMemberNewsCard(
                memberId = memberNewsCardRequest.memberId,
                newsCardId = memberNewsCardRequest.newsCardId,
            )
        )
    }
}
