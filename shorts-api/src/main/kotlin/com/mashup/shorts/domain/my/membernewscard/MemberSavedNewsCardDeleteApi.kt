package com.mashup.shorts.domain.my.membernewscard

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernewscard.MemberNewsCardDelete
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardBulkDeleteRequest
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping(("/v1/member/news-card"))
class MemberSavedNewsCardDeleteApi(
    private val memberNewsCardDelete: MemberNewsCardDelete
) {

    /**
    저장한 카드 뉴스 삭제 API
    @Body : {newsCardIds: List<Long>},
     */
    @Auth
    @PostMapping("/bulk-delete")
    fun bulkDeleteMemberNewsCard(
        @RequestBody memberNewsCardBulkDeleteRequest: MemberNewsCardBulkDeleteRequest,
    ): ApiResponse<Void> {
        memberNewsCardDelete.bulkDeleteMemberNewsCard(
            member = AuthContext.getMember(),
            newsCardIds = memberNewsCardBulkDeleteRequest.newsCardIds
        )
        return success(OK)
    }
}
