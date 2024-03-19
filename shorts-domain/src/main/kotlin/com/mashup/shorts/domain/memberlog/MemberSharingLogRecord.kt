package com.mashup.shorts.domain.memberlog

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.memberlogbadge.MemberLogBadgeService

@Service
@Transactional
class MemberSharingLogRecord(
    private val memberLogBadgeService: MemberLogBadgeService,
) {

    fun execute(member: Member) {
        memberLogBadgeService.memberSharingLog(member)
    }
}
