package com.mashup.shorts.common.response

import org.springframework.http.HttpStatus
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
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
