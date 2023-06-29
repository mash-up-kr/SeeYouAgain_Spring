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
import com.mashup.shorts.domain.my.membernewscard.dto.RetrieveAllNewsCardResponse
import com.mashup.shorts.domain.my.membernewscard.dto.SavedRetrieveNewsCardByMember
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardRetrieveApi(
    private val memberNewsCardRetrieve: MemberNewsCardRetrieve,
) {

    @Auth
    @GetMapping
    fun retrieveNewsCardByMember(
        @RequestParam targetDateTime: LocalDateTime,
        @RequestParam(
            defaultValue = "0",
            required = false
        ) @Min(0) @Max(Long.MAX_VALUE) cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
    ): ApiResponse<List<RetrieveAllNewsCardResponse>> {
        return success(
            OK,
            RetrieveAllNewsCardResponse.domainResponseFormToApiResponseForm(
                memberNewsCardRetrieve.retrieveNewsCardByMember(
                    member = AuthContext.getMember(),
                    targetDateTime = targetDateTime,
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }

    @Auth
    @GetMapping("/")
    fun retrieveSavedNewsCardByMember(
        @RequestParam(
            defaultValue = "0",
            required = false
        ) @Min(0) @Max(Long.MAX_VALUE) cursorId: Long,
        @RequestParam(required = true) @Min(1) @Max(20) size: Int,
    ): ApiResponse<SavedRetrieveNewsCardByMember> {
        return success(
            OK,
            SavedRetrieveNewsCardByMember.domainResponseFormToApiResponseForm(
                memberNewsCardRetrieve.retrieveSavedNewsCardByMember(
                    member = AuthContext.getMember(),
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }
}
