package com.mashup.shorts.domain.my.memberstatistics

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.my.memberstatistics.dto.MemberStatisticsResponse
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
    ): MemberStatisticsResponse {
        return MemberStatisticsResponse(
            targetDateTime = LocalDateTime.now(),
            statistics = memberStatisticsRetrieve.retrieveMemberStatisticsByMemberCategoryTargetDate(
                AuthContext.getMember(),
                targetDateTime
            )
        )
    }
}