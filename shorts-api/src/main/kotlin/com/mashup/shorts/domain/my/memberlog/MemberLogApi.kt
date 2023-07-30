package com.mashup.shorts.domain.my.memberlog

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.memberlog.MemberAttendance
import com.mashup.shorts.domain.memberlog.MemberSharing
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member/log")
class MemberLogApi(
    private val memberAttendance: MemberAttendance,
    private val memberSharing: MemberSharing
) {

    @PostMapping("/attendance")
    @Auth
    fun attendance(): ApiResponse<HttpStatus> {
        memberAttendance.execute(AuthContext.getMember())
        return ApiResponse.success(HttpStatus.OK)
    }

    @PostMapping("/sharing")
    @Auth
    fun memberSharingNews(): ApiResponse<HttpStatus> {
        memberSharing.execute(AuthContext.getMember())
        return ApiResponse.success(HttpStatus.OK)
    }
}