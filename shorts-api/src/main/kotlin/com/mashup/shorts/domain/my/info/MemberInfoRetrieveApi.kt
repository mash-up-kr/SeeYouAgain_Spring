package com.mashup.shorts.domain.my.info

import java.time.LocalDate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.my.MemberInfoRetrieve
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.my.info.dto.MemberInfoMapper
import com.mashup.shorts.domain.my.info.dto.MemberInfoRetrieveResponse

@RequestMapping("/v1/member/info")
@RestController
class MemberInfoRetrieveApi(
    private val memberInfoRetrieve: MemberInfoRetrieve
) {

    @Auth
    @GetMapping
    fun retrieveMemberInfo(): ApiResponse<MemberInfoRetrieveResponse> {
        val member = AuthContext.getMember()
        val memberInfo = memberInfoRetrieve.retrieveMemberInfo(member, LocalDate.now())
        return ApiResponse.success(HttpStatus.OK, MemberInfoMapper.memberInfoToResponse(memberInfo))
    }
}
