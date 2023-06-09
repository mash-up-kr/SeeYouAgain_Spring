package com.mashup.shorts.domain.member

import java.util.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.member.MemberRepository

/**
 * MemberCategoryCreate
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
@Service
class MemberCreate(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun createMember(memberUUID: String): Member {
        // TODO: 이미 존재하면 어떻게 처리할지 클라이언트와 논의 필요할 듯
        if (memberRepository.existsByUniqueId(memberUUID)) {
            throw ShortsBaseException.from(
                ShortsErrorCode.E409_CONFLICT,
                resultErrorMessage = "${memberUUID}에 해당하는 멤버 고유 값이 이미 존재하는 값입니다."
            )
        }

        return memberRepository.save(Member(
            uniqueId = memberUUID,
            nickname = Member.generateNickname()
        ))
    }
}
