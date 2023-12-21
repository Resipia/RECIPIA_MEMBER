package com.recipia.member.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User 커스텀 예외처리 클래스
 */
@Getter
@AllArgsConstructor
public class MemberApplicationException extends RuntimeException {

    private ErrorCode errorCode;

}