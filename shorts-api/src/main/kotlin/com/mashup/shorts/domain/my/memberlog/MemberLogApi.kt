package com.mashup.shorts.domain.my.memberlog

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.memberlog.MemberAttendanceLogRecord
import com.mashup.shorts.domain.memberlog.MemberSharingLogRecord
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member/log")
class MemberLogApi(
    private val memberAttendanceLogRecord: MemberAttendanceLogRecord,
    private val memberSharingLogRecord: MemberSharingLogRecord
) {

    @PostMapping("/attendance")
    @Auth
    fun attendance(): ApiResponse<HttpStatus> {
        memberAttendanceLogRecord.execute(AuthContext.getMember())
        return ApiResponse.success(HttpStatus.OK)
    }

    @PostMapping("/sharing")
    @Auth
    fun memberSharingNews(): ApiResponse<HttpStatus> {
        memberSharingLogRecord.execute(AuthContext.getMember())
        return ApiResponse.success(HttpStatus.OK)
    }
}
