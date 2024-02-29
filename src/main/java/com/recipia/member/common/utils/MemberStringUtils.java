package com.recipia.member.common.utils;

/**
 * SNS topicArn에서 topic name을 추출하기 위한 유틸리티 클래스
 */
public class MemberStringUtils {

    public static String extractLastPart(String input) {
        int lastIndex = input.lastIndexOf(":");
        if (lastIndex != -1) {
            return input.substring(lastIndex + 1);
        } else {
            return "";  // ':'이 없으면 빈 문자열 반환
        }
    }

}
