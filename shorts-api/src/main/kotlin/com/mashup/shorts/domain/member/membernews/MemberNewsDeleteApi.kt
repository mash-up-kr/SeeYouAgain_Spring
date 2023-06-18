package com.mashup.shorts.domain.member.membernews

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membernews.dto.MemberNewsDeleteBulkRequest

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsDeleteApi(
    private val memberNewsDelete: MemberNewsDelete
) {

    @Auth
    @PostMapping("/bulk-delete")
    fun deleteMemberNews(@RequestBody memberNewsDeleteBulkRequest: MemberNewsDeleteBulkRequest): ApiResponse<Void> {
        val memberUniqueId = AuthContext.getMemberId()
        memberNewsDelete.deleteMemberNews(memberUniqueId, memberNewsDeleteBulkRequest.newsIds)
        return ApiResponse.success(HttpStatus.OK)
    }
}
