package com.mashup.shorts.domain.my.membershowmode

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.membershowmode.MemberShowModeUpdate
import com.mashup.shorts.domain.my.info.dto.MemberChangeShowModeRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
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
            showModes = memberChangeShowModeRequest.showMode
        )

        return ApiResponse.success(HttpStatus.OK)
    }
}
