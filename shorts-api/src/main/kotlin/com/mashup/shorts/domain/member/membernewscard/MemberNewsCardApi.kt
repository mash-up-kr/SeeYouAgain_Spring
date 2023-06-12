package com.mashup.shorts.domain.member.membernewscard

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearResponse

@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardApi(
    private val memberNewsCardClear: MemberNewsCardClear,
) {

    @Auth
    @DeleteMapping("/{newsCardId}")
    fun deleteMemberNewsCard(
        @PathVariable newsCardId: Long,
    ): ApiResponse<Void> {
        val memberUniqueId = AuthContext.getMemberId()
        memberNewsCardClear.deleteMemberNewsCard(memberUniqueId, newsCardId)
        return success(OK)
    }

    @DeleteMapping
    fun clearMemberNewsCard(
        @RequestBody memberNewsCardRequest: MemberNewsCardClearRequest,
    ): ApiResponse<MemberNewsCardClearResponse> {
        return success(
            OK,
            MemberNewsCardClearResponse(
                memberNewsCardClear.clearMemberNewsCard(
                    memberId = memberNewsCardRequest.memberId,
                    newsCardId = memberNewsCardRequest.newsCardId,
                )
            )
        )
    }

}
