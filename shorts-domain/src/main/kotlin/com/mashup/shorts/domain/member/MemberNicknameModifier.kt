package com.mashup.shorts.domain.member

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberNicknameModifier {

    fun modifyMemberNickname(member: Member, nickname: String) {
        member.nickname = nickname
    }
}