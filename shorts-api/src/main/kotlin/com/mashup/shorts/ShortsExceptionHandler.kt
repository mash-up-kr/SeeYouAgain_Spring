package com.mashup.shorts

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.response.ErrorResponse
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * ShortsExceptionHandler
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 30.
 */
@RestControllerAdvice
class ShortsExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun httpRequestMethodNotSupportedException(exception: HttpRequestMethodNotSupportedException): ErrorResponse {
        log.warn(exception.message, exception)
        return ErrorResponse.of(ShortsErrorCode.E405_METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    private fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ErrorResponse {
        log.warn(e.message)
        if (e.rootCause is MissingKotlinParameterException) {
            val parameterName = (e.rootCause as MissingKotlinParameterException).parameter.name
            return ErrorResponse.of(ShortsErrorCode.E400_BAD_REQUEST, "Parameter ($parameterName) is Missing")
        }
        return ErrorResponse.of(ShortsErrorCode.E400_BAD_REQUEST)
    }

    @ExceptionHandler(ShortsBaseException::class)
    private fun handleBaseException(exception: ShortsBaseException): ErrorResponse {
        log.error(exception.resultErrorMessage, exception)
        return ErrorResponse.of(exception.shortsErrorCode, exception.resultErrorMessage)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    private fun handleInternalServerException(exception: Exception): ErrorResponse {
        log.error(exception.message, exception)
        return ErrorResponse.of(ShortsErrorCode.E500_INTERNAL_SERVER_ERROR)
    }
}