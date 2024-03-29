package com.recipia.member.adapter.in.web.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 공통으로 사용되는 ResponseDTO
 */
@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private String resultCode;
    private T result;

    /**
     * 반환받을 필요가 없을 때 사용되는 메서드이다.
     * 이 메서드는 결과 코드를 "SUCCESS"로 설정하고 결과를 null로 설정한 ResponseDto<Void> 객체를 반환한다.
     * 주로 create, update, delete와 같은 작업에서 사용된다.
     */
    public static ResponseDto<Void> success() {
        return new ResponseDto<>("SUCCESS", null);
    }

    /**
     * 결과 값을 응답받아야 할 때 사용되는 메서드이다.
     * 이 메서드는 결과 코드를 "SUCCESS"로 설정하고 결과를 인자로 받은 값으로 설정한 ResponseDto<T> 객체를 반환한다.
     */
    public static <T> ResponseDto<T> success(T result) {
        return new ResponseDto<>("SUCCESS", result);
    }


}
