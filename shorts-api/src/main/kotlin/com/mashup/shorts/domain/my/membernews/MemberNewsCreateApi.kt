package com.mashup.shorts.domain.my.membernews

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membernews.MemberNewsCreate
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsCreateApi(
    private val memberNewsCreate: MemberNewsCreate,
) {

    /**
    키워드로 조회한 뉴스 저장 API
    @Body : {newsId: Long}
     */
    @Auth
    @PostMapping("/keyword")
    fun createMemberNewsByKeyword(
        @RequestBody memberNewsCreateRequest: MemberNewsCreateRequest,
    ): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberNewsCreate.createMemberNewsByKeyword(member, memberNewsCreateRequest.newsId)
        return ApiResponse.success(HttpStatus.CREATED)
    }

    /**
    뉴스카드에서 조회한 뉴스 저장 API
    @Body : {newsId: Long}
     */
    @Auth
    @PostMapping("/newscard")
    fun createMemberNewsByNewsCard(
        @RequestBody memberNewsCreateRequest: MemberNewsCreateRequest,
    ): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberNewsCreate.createMemberNewsByNewsCard(member, memberNewsCreateRequest.newsId)
        return ApiResponse.success(HttpStatus.CREATED)
    }
}
