package com.mashup.shorts.domain.my.statistics

import java.time.LocalDate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.my.statistics.dto.MemberWeeklyStatsRetrieveResponse

@RestController
class MemberStatsRetrieveApi(
    private val memberStatsRetrieve: MemberStatsRetrieve
) {

    /*
    주간 숏스 통계 API
     */
    @Auth
    @GetMapping("/v1/member/weekly-stats")
    fun retrieveMemberStats(): ApiResponse<MemberWeeklyStatsRetrieveResponse> {
        val member = AuthContext.getMember()
        val memberWeeklyStats = memberStatsRetrieve.retrieveMemberWeeklyStats(member, LocalDate.now(), 4)
        return ApiResponse.success(HttpStatus.OK, MemberWeeklyStatsRetrieveResponse(
            weeklyShortsCnt = memberWeeklyStats.weeklyShortsCnt, dateOfShortsRead = memberWeeklyStats.dateOfShortsRead))
    }

}
