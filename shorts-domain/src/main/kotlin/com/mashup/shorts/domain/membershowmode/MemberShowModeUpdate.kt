package com.mashup.shorts.domain.membershowmode

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.ShowMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.memberlogbadge.MemberLogBadgeService

@Service
@Transactional
class MemberShowModeUpdate(
    private val memberLogBadgeService: MemberLogBadgeService
) {

    fun updateMemberShowMode(member: Member, showModes: List<ShowMode>) {
        showModes.map {
            memberLogBadgeService.updateMemberShowModeLog(member, it)
        }
    }
}
