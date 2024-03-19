package com.mashup.shorts.domain.my.membernews

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernews.MemberNewsDelete
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsDeleteBulkRequest
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/member/news")
@RestController
class MemberSavedNewsDeleteApi(
    private val memberNewsDelete: MemberNewsDelete,
) {

    /**
    저장한 뉴스 삭제 API
    @Body : {newsIds: List<Long>}
     */
    @Auth
    @PostMapping("/bulk-delete")
    fun deleteMemberNews(
        @RequestBody memberNewsDeleteBulkRequest: MemberNewsDeleteBulkRequest,
    ): ApiResponse<Void> {
        memberNewsDelete.deleteMemberNews(
            member = AuthContext.getMember(),
            newsIds = memberNewsDeleteBulkRequest.newsIds
        )
        return success(OK)
    }
}
