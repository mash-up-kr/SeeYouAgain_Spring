package com.mashup.shorts.domain.my.memberbadge

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.memberbadge.MemberBadgeRetrieve
import com.mashup.shorts.domain.my.memberbadge.dto.MemberBadgeRetrieveResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member/badge")
class MemberBadgeRetrieveApi(
    private val memberBadgeRetrieve: MemberBadgeRetrieve
) {

    @GetMapping
    @Auth
    fun retrieveMemberBadge(): ApiResponse<MemberBadgeRetrieveResponse> {
        val result = memberBadgeRetrieve.retrieveMemberBadge(AuthContext.getMember())

        return ApiResponse.success(
            HttpStatus.OK,
            MemberBadgeRetrieveResponse(
                threeDaysContinuousAttendance = result.threeDaysContinuousAttendance,
                tenDaysContinuousAttendance = result.threeDaysContinuousAttendance,
                explorer = result.explorer,
                kingOfSharing = result.kingOfSharing,
                firstAllReadShorts = result.firstClearNews,
                firstOldShortsSaving = result.firstNewsSaving,
                changeMode = result.changeMode
            )
        )
    }
}