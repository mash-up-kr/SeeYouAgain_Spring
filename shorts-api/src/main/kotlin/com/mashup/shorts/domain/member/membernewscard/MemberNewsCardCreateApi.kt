package com.mashup.shorts.domain.member.membernewscard

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardCreateRequest

@RequestMapping("/v1/member/news-card")
@RestController
class MemberNewsCardCreateApi(
    private val memberNewsCardCreate: MemberNewsCardCreate
) {

    @PostMapping
    fun createMemberNewsCard(@RequestHeader(value = "Authorization") uniqueId: String,
                             @RequestBody memberNewsCardCreateRequest: MemberNewsCardCreateRequest
    ): ApiResponse<Void> {
        memberNewsCardCreate.createMemberNewsCard(uniqueId, memberNewsCardCreateRequest.newsCardId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
