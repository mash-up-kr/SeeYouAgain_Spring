package com.mashup.shorts.domain.my.membernews

import java.time.LocalDate
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernews.MemberNewsRetrieve
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsResponse.Companion.persistenceToResponseForm
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsRetrieveResponse
import com.mashup.shorts.domain.newscard.Pivots
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/member-news")
class MemberNewsRetrieveApi(
    private val memberNewsRetrieve: MemberNewsRetrieve,
) {

    /**
    오래 간직할 뉴스 조회 API
    @Param : targetDate, cursorWrittenDateTime, size, pivot
     */
    @Auth
    @GetMapping
    fun retrieveNewsByMember(
        @RequestParam targetDate: LocalDate,
        @RequestParam cursorWrittenDateTime: String,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
        @RequestParam pivot: Pivots,
    ): ApiResponse<MemberNewsRetrieveResponse> {
        val member = AuthContext.getMember()
        return success(
            OK,
            MemberNewsRetrieveResponse(
                today = LocalDate.now(),
                savedNewsCount = memberNewsRetrieve.retrieveMemberNewsCount(member),
                memberNewsResponse = persistenceToResponseForm(
                    memberNewsRetrieve.retrieveMemberNews(
                        targetDate = targetDate,
                        member = member,
                        cursorWrittenDateTime = cursorWrittenDateTime,
                        size = size,
                        pivot = pivot
                    )
                )
            )
        )
    }
}
