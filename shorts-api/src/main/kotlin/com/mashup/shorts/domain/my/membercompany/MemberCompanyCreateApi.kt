package com.mashup.shorts.domain.my.membercompany

import com.mashup.shorts.annotation.Auth
import com.mashup.shorts.aspect.AuthContext
import com.mashup.shorts.response.ApiResponse
import com.mashup.shorts.domain.membercompany.MemberCompanyCreate
import com.mashup.shorts.domain.my.member.dto.MemberCompanyCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member")
class MemberCompanyCreateApi(
    private val memberCompanyCreate: MemberCompanyCreate
) {

    @Auth
    @PostMapping("/company")
    fun modifyNickname(
        @RequestBody memberCompanyCreateRequest: MemberCompanyCreateRequest
    ): ApiResponse<HttpStatus> {
        memberCompanyCreate.createMemberCompany(
            member = AuthContext.getMember(),
            companies = memberCompanyCreateRequest.companies
        )
        return ApiResponse.success(HttpStatus.OK)
    }
}
