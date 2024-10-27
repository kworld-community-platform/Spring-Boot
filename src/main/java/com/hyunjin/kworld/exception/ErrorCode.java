package com.hyunjin.kworld.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 멤버
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-01", "가입하지 않은 회원입니다"),

    // 멤버 - 로그인
    INVALID_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "LOGIN-01", "잘못된 로그인 요청입니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN-02", "인증에 실패했습니다."),
    LOGIN_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "LOGIN-03", "로그인 처리 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
