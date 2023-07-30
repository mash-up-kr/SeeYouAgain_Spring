package com.mashup.shorts.domain.memberbadge

import com.mashup.shorts.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberBadgeRetrieve(
    private val memberBadgeRepository: MemberBadgeRepository
) {

    fun retrieveMemberBadge(member: Member): MemberBadge {
        return memberBadgeRepository.findByMember(member)
    }
}