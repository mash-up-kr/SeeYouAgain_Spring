package com.mashup.shorts.domain.my.membernewscard

import java.time.LocalDateTime
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
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.my.membernewscard.dto.RetrieveHomeNewsCardResponse
import com.mashup.shorts.domain.my.membernewscard.dto.RetrieveHomeNewsCardResponse.Companion.domainResponseFormToApiResponseForm
import com.mashup.shorts.domain.my.membernewscard.dto.RetrieveSavedNewsCardResponse
import com.mashup.shorts.domain.my.membernewscard.dto.RetrieveSavedNewsCardResponse.Companion.domainResponseFormToApiResponseForm
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardRetrieveApi(
    private val memberNewsCardRetrieve: MemberNewsCardRetrieve,
) {

    /**
    홈 조회 API
    @Param : targetDateTime, cursorId, size
     */
    @Auth
    @GetMapping
    fun retrieveNewsCardByMember(
        @RequestParam targetDateTime: LocalDateTime,
    ): ApiResponse<List<RetrieveHomeNewsCardResponse>> {
        return success(
            OK,
            domainResponseFormToApiResponseForm(
                memberNewsCardRetrieve.retrieveNewsCardByMember(
                    member = AuthContext.getMember(),
                    targetDateTime = targetDateTime,
                )
            )
        )
    }

    /**
    저장한 오늘의 숏스(뉴스카드) 조회 API
    @Param : cursorId, size
     */
    @Auth
    @GetMapping("/saved")
    fun retrieveSavedNewsCardByMember(
        @RequestParam(
            defaultValue = "0",
            required = false
        ) @Min(0) @Max(Long.MAX_VALUE) cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
    ): ApiResponse<RetrieveSavedNewsCardResponse> {
        return success(
            OK,
            domainResponseFormToApiResponseForm(
                memberNewsCardRetrieve.retrieveSavedNewsCardByMember(
                    member = AuthContext.getMember(),
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }
}
