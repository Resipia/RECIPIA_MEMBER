package com.recipia.member.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 중앙 집중 예외처리를 위한 GlobalControllerAdvice 선언
 * RestController를 사용중이니 Advice도 @RestControllerAdvice를 사용한다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 에러 중앙처리 로직 작성완료
     */
    @ExceptionHandler(MemberApplicationException.class)
    public ResponseEntity<?> applicationHandler(MemberApplicationException e) {
        log.error("Error occurs {}", e.toString());

        // 에러 응답을 세팅한다.
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", e.getErrorCode().getCode());
        errorResponse.put("message", e.getErrorCode().getMessage());

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }


    /**
     * RuntimeException이 발생했을 때, 모든 RuntimeException을 INTERNAL_SERVER_ERROR로 간주하고 동일한 에러 코드와 메시지를 반환한다.
     * 이 방법은 모든 RuntimeException을 동일하게 처리하기 때문에, 예외 처리를 단순화하고 싶을 때 유용하다.
     * 그러나 이 방법은 NullPointerException과 같은 특정 RuntimeException의 세부 정보를 사용자에게 제공하지 못하므로, 디버깅이 어려울 수 있다.
     */
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> applicationHandler(RuntimeException e) {
//        log.error("Error occurs {}", e.toString());
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
//    }

    /**
     * 이 방식을 적용하면 조금 더 상세하게 에러 정보를 볼수가 있다. (NPE같은 특정 예외처리의 세부정보를 제공한다.)
     * 하지만 이 방법은 예외 메시지가 직접 노출되므로 주의해야 한다.
     * 최근에는 이렇게 사용하는게 조금 더 선호되는것 같다.(더 많은 정보를 제공하고, 필터링으로 보안문제를 해결할 수 있고, 유연하다.)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error occurs {}", e.toString());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", ErrorCode.RECIPE_SERVICE_ERROR.getCode());
        errorResponse.put("message", e.getMessage());

        return ResponseEntity.status(ErrorCode.RECIPE_SERVICE_ERROR.getStatus())
                .body(errorResponse);
    }

}
