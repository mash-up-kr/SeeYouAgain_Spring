package com.mashup.shorts.domain.my.membernewscard

import java.time.LocalDate
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernewscard.MemberNewsCardDelete
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardBulkDeleteRequest
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardClearRequest

@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardDeleteApi(
    private val memberNewsCardDelete: MemberNewsCardDelete,
) {

    /**
    오늘의 숏스 제거 API
    @Body : {newsCardIds: List<Long>},
     */
    @Auth
    @PostMapping
    fun bulkDeleteMemberNewsCard(
        @RequestBody memberNewsCardBulkDeleteRequest: MemberNewsCardBulkDeleteRequest,
    ): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberNewsCardDelete.bulkDeleteMemberNewsCard(
            member = member,
            newsCardIds = memberNewsCardBulkDeleteRequest.newsCardIds
        )
        return success(OK)
    }

    /**
    오늘의 숏스 다 읽었어요 API
    @Body : {newsCardIds: Long},
     */
    @Auth
    @DeleteMapping
    fun clearMemberNewsCard(
        @RequestBody memberNewsCardClearRequest: MemberNewsCardClearRequest,
    ): ApiResponse<Map<String, Int>> {
        return success(
            OK,
            memberNewsCardDelete.clearMemberNewsCard(
                member = AuthContext.getMember(),
                today = LocalDate.now(),
                newsCardId = memberNewsCardClearRequest.newsCardId
            )
        )
    }
}
