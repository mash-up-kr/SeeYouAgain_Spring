package com.mashup.shorts.domain.my.membernews

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membernews.MemberNewsDelete
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsDeleteBulkRequest

@RequestMapping("/v1/member/news")
@RestController
class MemberNewsDeleteApi(
    private val memberNewsDelete: MemberNewsDelete
) {

    @Auth
    @PostMapping("/bulk-delete")
    fun deleteMemberNews(@RequestBody memberNewsDeleteBulkRequest: MemberNewsDeleteBulkRequest): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberNewsDelete.deleteMemberNews(member, memberNewsDeleteBulkRequest.newsIds)
        return ApiResponse.success(HttpStatus.OK)
    }
}
