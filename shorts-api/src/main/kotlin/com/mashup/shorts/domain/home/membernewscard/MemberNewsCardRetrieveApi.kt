package com.mashup.shorts.domain.home.membernewscard

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.home.membernewscard.dto.HomeResponse
import com.mashup.shorts.domain.home.membernewscard.dto.HomeResponse.Companion.makeHomeResponse
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRetrieve
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Validated
@RestController
@RequestMapping("/v1/member/news-card")
class MemberNewsCardRetrieveApi(
    private val memberNewsCardRetrieve: MemberNewsCardRetrieve,
) {

    /**
    홈 조회 API
    @Param : targetDateTime
     */
    @Auth
    @GetMapping
    fun retrieveNewsCardByMember(
        @RequestParam targetDateTime: LocalDateTime,
    ): ApiResponse<HomeResponse> {
        return success(
            OK,
            makeHomeResponse(
                showMode = AuthContext.getMember().showMode,
                newsCards = memberNewsCardRetrieve.retrieveHome(
                    member = AuthContext.getMember(),
                    targetDateTime = targetDateTime,
                )
            )
        )
    }
}
