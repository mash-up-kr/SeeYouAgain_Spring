package com.mashup.shorts.domain.my.info

import java.time.LocalDate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.my.MemberInfoRetrieve
import com.mashup.shorts.domain.my.info.dto.MemberInfoMapper
import com.mashup.shorts.domain.my.info.dto.MemberInfoRetrieveResponse

@RequestMapping("/v1/member/info")
@RestController
class MemberInfoRetrieveApi(
    private val memberInfoRetrieve: MemberInfoRetrieve,
) {

    /**
    유저 정보 조회 API
     */
    @Auth
    @GetMapping
    fun retrieveMemberInfo(): ApiResponse<MemberInfoRetrieveResponse> {
        val member = AuthContext.getMember()
        val memberInfo = memberInfoRetrieve.retrieveMemberInfo(member, LocalDate.now())
        return ApiResponse.success(HttpStatus.OK, MemberInfoMapper.memberInfoToResponse(memberInfo))
    }
}
