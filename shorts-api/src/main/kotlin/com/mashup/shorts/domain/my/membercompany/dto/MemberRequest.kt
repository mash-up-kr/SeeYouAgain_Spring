package com.mashup.shorts.domain.my.member.dto

import com.mashup.shorts.domain.member.Company

data class MemberCompanyCreateRequest(
    val companies: List<Company>
)