package com.mashup.shorts.common.response

import com.mashup.shorts.common.exception.ShortsErrorCode

data class ErrorResponse(
    val status: Int,
    val error: Error
) {
    companion object {
        fun of(shortsErrorCode: ShortsErrorCode, detailMessage: String ?= null): ErrorResponse {
            return ErrorResponse(
                status = shortsErrorCode.httpStatus.value(),
                error = Error(
                    code = shortsErrorCode.errorCode,
                    detailMessage = detailMessage
                )
            )
        }
    }
}

data class Error(
    val code: String,
    val detailMessage: String?
)
