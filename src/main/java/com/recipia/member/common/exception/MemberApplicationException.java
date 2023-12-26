package com.recipia.member.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Member 커스텀 예외처리 클래스
 */
@Getter
@NoArgsConstructor
public class MemberApplicationException extends RuntimeException {

    private ErrorCode errorCode;

    public MemberApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}