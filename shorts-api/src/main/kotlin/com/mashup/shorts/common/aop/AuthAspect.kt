package com.mashup.shorts.common.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.MemberRepository
import jakarta.servlet.http.HttpServletRequest

@Aspect
@Component
class AuthAspect(
    private val httpServletRequest: HttpServletRequest,
    private val memberRepository: MemberRepository
) {

    @Around("@annotation($SHORTS_PACKAGE)")
    fun memberId(pjp: ProceedingJoinPoint): Any {
        val memberId = resolveToken(httpServletRequest)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E401_UNAUTHORIZED,
                "Request Header에 memberId가 존재하지 않습니다."
            )

        val member = memberRepository.findByUniqueId(memberId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "존재하지 않는 유저입니다. memberId : $memberId"
            )

        AuthContext.USER_CONTEXT.set(member)
        return pjp.proceed(pjp.args)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX_BEARER)) {
            return bearerToken.substring(7)
        }

        return null
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val PREFIX_BEARER = "Bearer "
        private const val SHORTS_PACKAGE = "com.mashup.shorts.common.aop.Auth"
    }
}
