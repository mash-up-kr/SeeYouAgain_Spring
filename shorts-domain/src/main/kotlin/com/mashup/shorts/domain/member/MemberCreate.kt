package com.mashup.shorts.domain.member

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.memberlog.MemberLog
import com.mashup.shorts.domain.memberlog.MemberLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberCreate(
    private val memberRepository: MemberRepository,
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService,
) {

    @Transactional
    fun createMember(memberUUID: String, fcmTokenPayload: String): Member {
        // TODO: 이미 존재하면 어떻게 처리할지 클라이언트와 논의 필요할 듯
        if (memberRepository.existsByUniqueId(memberUUID)) {
            throw ShortsBaseException.from(
                ShortsErrorCode.E409_CONFLICT,
                resultErrorMessage = "${memberUUID}에 해당하는 멤버 고유 값이 이미 존재하는 값입니다."
            )
        }

        val member = Member(
            uniqueId = memberUUID,
            nickname = Member.generateNickname(),
            fcmTokenPayload = fcmTokenPayload
        )

        memberLogBadgeFacadeService.createLogAndBadgeByMember(member)
        return memberRepository.save(member)
    }
}
