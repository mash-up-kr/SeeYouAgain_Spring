package com.mashup.shorts.common.response

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val status: Int,

    val result: T? = null,
) {
    companion object {

        fun <T> success(httpStatus: HttpStatus): ApiResponse<T> {
            return ApiResponse(httpStatus.value(), null)
        }

        fun <T> success(httpStatus: HttpStatus, result: T) = ApiResponse(httpStatus.value(), result)
    }
}
