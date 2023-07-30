package com.mashup.shorts.domain.membershowmode

import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.ShowMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberShowModeUpdate(
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService
) {

    fun updateMemberShowMode(member: Member, showMode: ShowMode) {
        memberLogBadgeFacadeService.updateMemberShowModeLog(member, showMode)
    }
}