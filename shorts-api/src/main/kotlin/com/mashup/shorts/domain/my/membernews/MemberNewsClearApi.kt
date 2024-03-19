package com.mashup.shorts.domain.my.membernews

import java.time.LocalDateTime
import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
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
            newsId = memberNewsReadRequest.newsId,
            now = LocalDateTime.now()
        )

        return ApiResponse.success(HttpStatus.OK)
    }
}
