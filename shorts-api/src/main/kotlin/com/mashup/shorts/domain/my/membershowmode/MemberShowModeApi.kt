package com.mashup.shorts.domain.my.membershowmode

import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.membershowmode.MemberShowModeUpdate
import com.mashup.shorts.domain.my.info.dto.MemberChangeShowModeRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member/show-mode")
class MemberShowModeApi(
    private val memberShowModeUpdate: MemberShowModeUpdate
) {

    @PatchMapping
    @Auth
    fun changeMemberShowMode(
        @RequestBody memberChangeShowModeRequest: MemberChangeShowModeRequest
    ): ApiResponse<HttpStatus> {
        memberShowModeUpdate.updateMemberShowMode(
            member = AuthContext.getMember(),
            showMode = memberChangeShowModeRequest.showMode
        )

        return ApiResponse.success(HttpStatus.OK)
    }
}