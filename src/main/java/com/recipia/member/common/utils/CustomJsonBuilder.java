package com.recipia.member.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Json 문자열을 동적으로 생성해주는 유틸리티 클래스
 *
 */
@Component
public class CustomJsonBuilder {

    private Map<String, Object> values = new HashMap<>();

    public CustomJsonBuilder add(String key, Object value) {
        values.put(key, value);
        return this;
    }

    public String build() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(values);
    }

}
