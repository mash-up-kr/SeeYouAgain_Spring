package com.mashup.shorts.domain.my.member

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.member.MemberNicknameModifier
import com.mashup.shorts.domain.my.member.dto.MemberNicknameUpdateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member")
class MemberNicknameUpdateApi(
    private val memberNicknameModifier: MemberNicknameModifier
) {


    @Auth
    @PatchMapping("/nickname")
    fun modifyNickname(
        @RequestBody memberNicknameUpdateRequest: MemberNicknameUpdateRequest
    ): ApiResponse<HttpStatus> {
        memberNicknameModifier.modifyMemberNickname(
            member = AuthContext.getMember(),
            nickname = memberNicknameUpdateRequest.nickname
        )
        return ApiResponse.success(HttpStatus.OK)
    }
}
