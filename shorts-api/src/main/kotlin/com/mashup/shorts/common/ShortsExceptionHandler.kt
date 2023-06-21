package com.mashup.shorts.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.exception.ShortsErrorCode.E400_BAD_REQUEST
import com.mashup.shorts.common.exception.ShortsErrorCode.E500_INTERNAL_SERVER_ERROR
import com.mashup.shorts.common.response.ErrorResponse
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import jakarta.validation.ConstraintViolationException

@RestControllerAdvice
class ShortsExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    private fun handlerConstraintViolationException(
        exception: ConstraintViolationException,
    ): ErrorResponse {
        log.warn(exception.message)
        return ErrorResponse.of(
            shortsErrorCode = E400_BAD_REQUEST,
            detailMessage = "Please Check Your Request Parameter"
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    private fun handleMethodArgumentTypeMismatchException(
        exception: MethodArgumentTypeMismatchException,
    ): ErrorResponse {
        log.warn(exception.message)
        return ErrorResponse.of(shortsErrorCode = E400_BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    private fun handleMissingServletRequestParameterException(
        exception: MissingServletRequestParameterException,
    ): ErrorResponse {
        log.warn(exception.message)
        return ErrorResponse.of(
            shortsErrorCode = E400_BAD_REQUEST,
            detailMessage = "Please Check Your Request Parameter"
        )
    }

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
            return ErrorResponse.of(E400_BAD_REQUEST, "Parameter ($parameterName) is Missing")
        }
        return ErrorResponse.of(E400_BAD_REQUEST)
    }

    @ExceptionHandler(ShortsBaseException::class)
    private fun handleBaseException(exception: ShortsBaseException): ResponseEntity<ErrorResponse> {
        log.error(exception.resultErrorMessage, exception)
        return ResponseEntity.status(exception.shortsErrorCode.httpStatus)
            .body(exception.toErrorResponse())
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    private fun handleInternalServerException(exception: Exception): ErrorResponse {
        log.error(exception.message, exception)
        return ErrorResponse.of(E500_INTERNAL_SERVER_ERROR)
    }
}
