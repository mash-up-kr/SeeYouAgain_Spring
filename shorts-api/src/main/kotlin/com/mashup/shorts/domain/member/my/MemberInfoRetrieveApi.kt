package com.mashup.shorts.domain.member.my

import java.time.LocalDate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.my.dto.MemberInfoMapper
import com.mashup.shorts.domain.member.my.dto.MemberInfoRetrieveResponse

@RequestMapping("/v1/member/info")
@RestController
class MemberInfoRetrieveApi(
    private val memberInfoRetrieve: MemberInfoRetrieve
) {

    @Auth
    @GetMapping
    fun retrieveMemberInfo(): ApiResponse<MemberInfoRetrieveResponse> {
        val memberUniqueId = AuthContext.getMemberId()
        val memberInfo = memberInfoRetrieve.retrieveMemberInfo(memberUniqueId, LocalDate.now())
        return ApiResponse.success(HttpStatus.OK, MemberInfoMapper.memberInfoToResponse(memberInfo))
    }
}
