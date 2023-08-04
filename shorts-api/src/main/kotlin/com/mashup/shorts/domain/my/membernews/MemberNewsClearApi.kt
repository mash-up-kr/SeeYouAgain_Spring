package com.mashup.shorts.domain.my.membernews

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membernews.MemberNewsRead
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsReadRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsClearApi(
    private val memberNewsRead: MemberNewsRead
) {

    @PostMapping("/read")
    @Auth
    fun readMemberNews(
        @RequestBody memberNewsReadRequest: MemberNewsReadRequest
    ): ApiResponse<HttpStatus> {
        memberNewsRead.clearNewsCard(
            member = AuthContext.getMember(),
            newsId = memberNewsReadRequest.newsId
        )

        return ApiResponse.success(HttpStatus.OK)
    }
}