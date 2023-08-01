package com.mashup.shorts.domain.memberlog

import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberAttendanceLogRecord(
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService
) {

    fun execute(member: Member) {
        memberLogBadgeFacadeService.memberAttendanceLog(member)
    }
}