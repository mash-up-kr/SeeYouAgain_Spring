package com.mashup.shorts.domain.my.membernews

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernews.MemberNewsRetrieve
import com.mashup.shorts.domain.membernews.SavedFlag
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsResponse.Companion.makeMemberNewsResponse
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsRetrieveByCompanyResponse
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
@RequestMapping("/v1/member-news")
class MemberNewsRetrieveApi(
    private val memberNewsRetrieve: MemberNewsRetrieve,
) {

    /**
    키워드로 저장한 뉴스 조회
    @Param : cursorWrittenDateTime, size, pivot
     */
    @Auth
    @GetMapping("/keyword")
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
                        savedFlag = SavedFlag.KEYWORD
                    )
                )
            )
        )
    }

    /**
    뉴스 카드에서 저장한 뉴스 조회
    @Param : targetDate, cursorWrittenDateTime, size, pivot
     */
    @Auth
    @GetMapping("/newscard")
    fun retrieveMemberNewsBySavingNewsCard(
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
                        savedFlag = SavedFlag.NEWS_CARD
                    )
                )
            )
        )
    }

    /**
    회사 이름으로 뉴스 검색 API
    @Param : cursorId, pivot
     */
    @Auth
    @GetMapping("/company")
    fun retrieveMemberNewsBySavingKeyword(
        @RequestParam(defaultValue = "0", required = false) @Min(0) @Max(Long.MAX_VALUE)
        cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
    ): ApiResponse<MemberNewsRetrieveByCompanyResponse> {
        val member = AuthContext.getMember()
        return success(
            OK,
            MemberNewsRetrieveByCompanyResponse(
                makeMemberNewsResponse(
                    memberNewsRetrieve.retrieveNewsByMemberCompany(
                        member = member,
                        cursorId = cursorId,
                        size = size
                    )
                )
            )
        )
    }
}
