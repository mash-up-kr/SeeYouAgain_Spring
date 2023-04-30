package com.mashup.shorts.common.exception

/**
 * ShortsBaseException
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 30.
 */
class ShortsBaseException(
    val shortsErrorCode: ShortsErrorCode,
    val resultErrorMessage: String,    // 클라이언트에게 보여줄 에러 메세지
    override val cause: Throwable?,
) : RuntimeException(
    shortsErrorCode.toString(),
    cause
) {
    companion object {
        fun from(
            shortsErrorCode: ShortsErrorCode,
            resultErrorMessage: String,
            cause: Throwable? = null
        ): ShortsBaseException {
            return ShortsBaseException(
                shortsErrorCode = shortsErrorCode,
                resultErrorMessage = resultErrorMessage,
                cause = cause
            )
        }
    }
}