package com.mashup.shorts.domain.my.membernewscard

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membernewscard.MemberNewsCardCreate
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardCreateRequest

@RequestMapping("/v1/member/news-card")
@RestController
class MemberNewsCardCreateApi(
    private val memberNewsCardCreate: MemberNewsCardCreate,
) {

    /**
     오늘의 숏스 추가 API
     @Body : {newsCardId: Long},
     */
    @Auth
    @PostMapping
    fun createMemberNewsCard(
        @RequestBody memberNewsCardCreateRequest: MemberNewsCardCreateRequest,
    ): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberNewsCardCreate.createMemberNewsCard(member, memberNewsCardCreateRequest.newsCardId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
