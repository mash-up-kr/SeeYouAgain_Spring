package com.mashup.shorts.domain.member.membernewscard

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardCreateRequest

@RequestMapping("/v1/member/news-card")
@RestController
class MemberNewsCardCreateApi(
    private val memberNewsCardCreate: MemberNewsCardCreate,
) {

    @Auth
    @PostMapping
    fun createMemberNewsCard(@RequestBody memberNewsCardCreateRequest: MemberNewsCardCreateRequest): ApiResponse<Void> {
        val memberUniqueId = AuthContext.getMemberId()
        memberNewsCardCreate.createMemberNewsCard(memberUniqueId, memberNewsCardCreateRequest.newsCardId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
