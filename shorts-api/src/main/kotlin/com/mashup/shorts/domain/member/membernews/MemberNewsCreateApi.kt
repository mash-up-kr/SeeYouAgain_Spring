package com.mashup.shorts.domain.member.membernews

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membernews.dto.MemberNewsCreateRequest

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsCreateApi(
    private val memberNewsCreate: MemberNewsCreate
) {

    @Auth
    @PostMapping
    fun createMemberNews(@RequestHeader("Authorization") uniqueId: String,
                         @RequestBody memberNewsCreateRequest: MemberNewsCreateRequest
    ): ApiResponse<Void> {
        val memberUniqueId = AuthContext.getMemberId()
        memberNewsCreate.createMemberNews(memberUniqueId, memberNewsCreateRequest.newsId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
