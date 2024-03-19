package com.mashup.shorts.exception

class ShortsBaseException(
    val shortsErrorCode: ShortsErrorCode,
    val resultErrorMessage: String,
    override val cause: Throwable?,
) : RuntimeException(
    shortsErrorCode.toString(),
    cause
) {

    fun toErrorResponse(): ErrorResponse {
        return ErrorResponse(
            status = shortsErrorCode.httpStatus.value(),
            error = Error(
                code = shortsErrorCode.errorCode,
                detailMessage = shortsErrorCode.errorMessage
            )
        )
    }

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
