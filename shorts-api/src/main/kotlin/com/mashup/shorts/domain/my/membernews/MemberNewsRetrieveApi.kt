package com.mashup.shorts.domain.my.membernews

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernews.MemberNewsRetrieve
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsResponse.Companion.makeMemberNewsResponse
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsRetrieveResponse
import com.mashup.shorts.domain.newscard.Pivots
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/v1/member/news")
class MemberNewsRetrieveApi(
    private val memberNewsRetrieve: MemberNewsRetrieve,
) {

    /**
    저장한 뉴스 조회
    @Param : cursorWrittenDateTime, size, pivot
     */
    @Auth
    @GetMapping
    fun retrieveMemberNewsBySavingKeyword(
        @RequestParam cursorWrittenDateTime: String,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
        @RequestParam(required = true) pivot: Pivots,
    ): ApiResponse<MemberNewsRetrieveResponse> {
        val member = AuthContext.getMember()
        return success(
            OK,
            MemberNewsRetrieveResponse(
                savedNewsCount = memberNewsRetrieve.retrieveMemberNewsCount(member),
                memberNewsResponse = makeMemberNewsResponse(
                    newsBundle = memberNewsRetrieve.retrieveMemberNewsBySorting(
                        member = member,
                        cursorWrittenDateTime = cursorWrittenDateTime,
                        size = size,
                        pivot = pivot,
                    )
                )
            )
        )
    }
}
