package com.mashup.shorts.domain.my.membernews

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membernews.MemberNewsClear
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsClearRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsClearApi(
    private val memberNewsClear: MemberNewsClear
) {

    @PostMapping("/clear")
    @Auth
    fun clearMemberNews(@RequestBody memberNewsClearRequest: MemberNewsClearRequest): ApiResponse<HttpStatus> {
        memberNewsClear.clearNews(
            member = AuthContext.getMember(),
            newsId = memberNewsClearRequest.newsId
        )

        return ApiResponse.success(HttpStatus.OK)
    }
}