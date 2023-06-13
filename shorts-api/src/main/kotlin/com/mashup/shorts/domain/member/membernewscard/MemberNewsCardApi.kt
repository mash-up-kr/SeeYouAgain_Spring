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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardApi(
    private val memberNewsCardClear: MemberNewsCardClear,
) {

    @Operation(summary = "오늘의 숏스 단일 삭제", description = "유저와 삭제할 뉴스 카드의 id를 바탕으로 삭제")
    @Auth
    @DeleteMapping("/{newsCardId}")
    fun deleteMemberNewsCard(
        @PathVariable newsCardId: Long,
    ): ApiResponse<Void> {
        val memberUniqueId = AuthContext.getMemberId()
        memberNewsCardClear.deleteMemberNewsCard(memberUniqueId, newsCardId)
        return success(OK)
    }

    @Operation(summary = "오늘의 숏스 다 읽었어요", description = "유저의 모든 뉴스카드를 삭제")
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
