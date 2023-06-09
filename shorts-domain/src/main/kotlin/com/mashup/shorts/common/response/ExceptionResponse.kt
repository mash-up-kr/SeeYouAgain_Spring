package com.mashup.shorts.common.response

import com.mashup.shorts.common.exception.ShortsErrorCode

/**
 * ExceptionResponse
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 30.
 */
data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
) {
    companion object {
        fun of(shortsErrorCode: ShortsErrorCode, errorMessage: String? = null): ErrorResponse {
            return ErrorResponse(
                status = shortsErrorCode.httpStatus.value(),
                code = shortsErrorCode.errorCode,
                message = errorMessage ?: shortsErrorCode.errorMessage,
            )
        }
    }
}
