package com.mashup.shorts.domain.member.my.dto

import com.mashup.shorts.domain.member.my.MemberInfo

class MemberInfoMapper {

    companion object {

        fun memberInfoToResponse(memberInfo: MemberInfo): MemberInfoRetrieveResponse {
            return MemberInfoRetrieveResponse(
                nickname = memberInfo.nickname,
                joinPeriod = memberInfo.joinPeriod,
                totalShortsThisMonth = memberInfo.totalShortsThisMonth,
                todayShorts = memberInfo.todayShorts,
                savedShorts = memberInfo.savedShorts
            )
        }
    }
}
