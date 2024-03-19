package com.mashup.shorts.domain.my.statistics

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.response.ApiResponse.Companion.success
import com.mashup.shorts.domain.my.statistics.dto.MemberWeeklyStatsRetrieveResponse
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

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
        val memberWeeklyStats = memberStatsRetrieve.retrieveMemberWeeklyStats(
            member = AuthContext.getMember(),
            now = LocalDate.now()
        )
        return success(
            OK,
            MemberWeeklyStatsRetrieveResponse(
                weeklyShortsCnt = memberWeeklyStats.weeklyShortsCnt,
                categoryOfInterest = memberWeeklyStats.categoryOfInterest,
                dateOfShortsRead = memberWeeklyStats.dateOfShortsRead
            )
        )
    }
}
