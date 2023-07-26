package com.mashup.shorts.domain.my.memberstatistics

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.common.response.ApiResponse.Companion.success
import com.mashup.shorts.common.util.convert
import com.mashup.shorts.domain.my.memberstatistics.dto.MemberStatisticsResponse
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Validated
@RestController
@RequestMapping("/v1/member/statistics")
class MemberStatisticsRetrieveApi(
    private val memberStatisticsRetrieve: MemberStatisticsRetrieve
) {

    @GetMapping
    @Auth
    fun retrieveStatistics(
        @RequestParam("targetDateTime") targetDateTime: LocalDateTime,
    ): ApiResponse<MemberStatisticsResponse> {
        return success(
            OK,
            MemberStatisticsResponse(
            targetDateTime = convert(targetDateTime),
            statistics = memberStatisticsRetrieve.retrieveMemberStatisticsByMemberAndTargetDate(
                AuthContext.getMember(),
                targetDateTime
            ))
        )
    }
}