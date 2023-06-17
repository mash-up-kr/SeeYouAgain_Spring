package com.mashup.shorts.domain.member.membernewscard

import java.time.LocalDateTime
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membernewscard.dto.RetrieveAllNewsCardResponse
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@RestController
@RequestMapping("/v1/member-news-card")
class MemberNewsCardRetrieveApi(
    private val memberNewsCardRetrieve: MemberNewsCardRetrieve,
) {

    @Auth
    @GetMapping
    fun retrieveNewsCardByMember(
        @RequestParam targetDateTime: LocalDateTime,
        @RequestParam @Min(0) @Max(Long.MAX_VALUE) cursorId: Long,
        @RequestParam @Min(1) @Max(10) size: Int,
    ): ApiResponse<List<RetrieveAllNewsCardResponse>> {
        return success(
            OK,
            RetrieveAllNewsCardResponse.domainResponseFormToApiResponseForm(
                memberNewsCardRetrieve.retrieveNewsCardByMember(
                    memberUniqueId = AuthContext.getMemberId(),
                    targetDateTime = targetDateTime,
                    cursorId = cursorId,
                    size = size
                )
            )
        )
    }
}
