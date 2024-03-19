package com.mashup.shorts.aspect

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode

object AuthContext {

    val USER_CONTEXT: ThreadLocal<Member> = ThreadLocal()

    fun getMember(): Member {
        USER_CONTEXT.get()?.let {
            return USER_CONTEXT.get()
        } ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E401_UNAUTHORIZED,
            resultErrorMessage = "인증 체크 중에 ThreadLocal 값을 꺼내오는 중에 문제가 발생했습니다."
        )
    }
}
