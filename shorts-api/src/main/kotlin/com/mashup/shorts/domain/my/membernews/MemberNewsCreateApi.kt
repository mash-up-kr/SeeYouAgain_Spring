package com.mashup.shorts.domain.my.membernews

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membernews.MemberNewsCreate
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsCreateRequest

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsCreateApi(
    private val memberNewsCreate: MemberNewsCreate
) {

    @Auth
    @PostMapping
    fun createMemberNews(@RequestBody memberNewsCreateRequest: MemberNewsCreateRequest): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberNewsCreate.createMemberNews(member, memberNewsCreateRequest.newsId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}