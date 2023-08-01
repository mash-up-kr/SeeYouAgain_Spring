package com.mashup.shorts.domain.my.info.dto

import com.mashup.shorts.domain.my.MemberInfo

class MemberInfoMapper {

    companion object {

        fun memberInfoToResponse(memberInfo: MemberInfo): MemberInfoRetrieveResponse {
            return MemberInfoRetrieveResponse(
                nickname = memberInfo.nickname,
                joinPeriod = memberInfo.joinPeriod,
                totalSavedShortCount = memberInfo.totalSavedShortCount,
                savedNewsCountByKeyword = memberInfo.savedNewsCountByKeyword,
                savedNewsCountByNewsCard = memberInfo.savedNewsCountByNewsCard
            )
        }
    }
}
