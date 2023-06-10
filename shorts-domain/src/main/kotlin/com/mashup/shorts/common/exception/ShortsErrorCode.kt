package com.mashup.shorts.common.exception

import org.springframework.http.HttpStatus

/**
 * ShortsErrorCode
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 30.
 */
enum class ShortsErrorCode(
    val httpStatus: HttpStatus,
    val errorCode: String,
    val errorMessage: String,
) {

    E400_BAD_REQUEST(HttpStatus.BAD_REQUEST, "000", "필수 파라미터 값이 없거나 잘못된 값으로 요청을 보낸 경우 발생"),

    // ------------------------------ 401 ------------------------------
    E401_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "000", "유효하지 않은 인증 토큰을 사용한 경우 발생"),

    // ------------------------------ 403 ------------------------------
    E403_FORBIDDEN(HttpStatus.FORBIDDEN, "000", "사용 권한이 없는 경우 발생"),

    // ------------------------------ 404 ------------------------------
    E404_NOT_FOUND(HttpStatus.NOT_FOUND, "000", "요청한 리소스가 존재하지 않는 경우 발생"),

    // ------------------------------ 405 ------------------------------
    E405_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "000", "HTTP Method가 잘못된 경우"),


    // ------------------------------ 409 ------------------------------
    E409_CONFLICT(HttpStatus.CONFLICT, "000", "요청한 리소스가 중복된 경우 발생"),
    E409_UUID_CONFLICT(HttpStatus.CONFLICT, "001", "멤버 고유 아이디가 중복되었을 경우 발생"),

    // ------------------------------ 500 ------------------------------
    E500_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "000", "서버 내부에 문제 발생"),

    // ------------------------------ 501 ------------------------------
    E501_NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "000", "지원하지 않는 타입의 요청"),
}
