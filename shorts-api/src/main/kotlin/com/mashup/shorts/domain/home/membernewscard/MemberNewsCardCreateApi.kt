package com.mashup.shorts.domain.home.membernewscard

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.home.membernewscard.dto.MemberNewsCardCreateRequest
import com.mashup.shorts.domain.membernewscard.MemberNewsCardCreate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member/news-card")
class MemberNewsCardCreateApi(
    private val memberNewsCardCreate: MemberNewsCardCreate
) {

    /**
    뉴스카드 저장 API
    @Body : {newsId: Long}
     */
    @Auth
    @PostMapping
    fun createMemberNewsByNewsCard(
        @RequestBody memberNewsCardCreateRequest: MemberNewsCardCreateRequest
    ): ApiResponse<HttpStatus> {
        memberNewsCardCreate.createMemberNewsCard(
            member = AuthContext.getMember(),
            newsCardId = memberNewsCardCreateRequest.newsCardId
        )
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
