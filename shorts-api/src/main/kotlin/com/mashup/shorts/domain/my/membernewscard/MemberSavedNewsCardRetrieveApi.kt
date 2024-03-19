package com.mashup.shorts.domain.my.membernewscard

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardRetrieveResponse
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardRetrieveResponse.Companion.newsCardsToResponseForm
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
@RequestMapping("/v1/member/news-card")
class MemberSavedNewsCardRetrieveApi(
    private val memberNewsCardRetrieve: MemberNewsCardRetrieve
) {

    /**
    저장한 뉴스카드 조회
    @Param : targetDate, size, pivot
     */
    @Auth
    @GetMapping("/saved")
    fun retrieveMemberNewsBySavingNewsCard(
        @RequestParam cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
        @RequestParam(required = true) pivot: Pivots,
    ): ApiResponse<MemberNewsCardRetrieveResponse> {
        val newsCardByMember = memberNewsCardRetrieve.retrieveSavedNewsCardByMember(
            member = AuthContext.getMember(),
            cursorId = cursorId,
            size,
            pivot
        )

        return success(
            OK,
            newsCardsToResponseForm(
                newsCard = newsCardByMember.first,
                allMemberNewsCardCount = newsCardByMember.second
            )
        )
    }
}
