package com.recipia.member.config.jwt;

import com.recipia.member.dto.MemberDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 관련 토큰 Util
 */
@Slf4j
@Component
public class TokenUtils {


    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
    // jwtSecretKey를 바이트 배열로 변환하고, 이를 사용하여 HMAC-SHA256 알고리즘에 사용할 키를 생성한다.
    private static final Key key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    private static final String JWT_TYPE = "JWT";
    private static final String ALGORITHM = "HS256";
    private static final String USERNAME = "username";
    private static final String NICKNAME = "nickname";
    private static final String ROLE = "role";


    /**
     * 사용자 pk를 기준으로 JWT 토큰을 발급하여 반환해 준다.
     */
    public static String generateJwtToken(MemberDto memberDto) {

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                                  // Header 구성
                .setClaims(createClaims(memberDto))                           // Payload - Claims구성
                .setSubject(String.valueOf(memberDto.username()))              // Payload - Subjects구성
                .setIssuer("Recipia")                                       // Issuer 구성
                .signWith(key, SignatureAlgorithm.HS256)                    // Signature 구성 : 이 키를 사용하여 JWT 토큰에 서명을 추가한다. 이 서명은 토큰의 무결성을 보장하는 데 사용된다.
                .setExpiration(createExpiredDate());                        // Expired Date 구성

        log.info("generateJwtToken - Token generated for username: " + memberDto.username());
        return builder.compact();
    }

    /**
     * 토큰을 기반으로 사용자의 정보를 반환해주는 메서드
     */
    public static boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            log.info("isValidToken - Token is valid for username: " + claims.get(USERNAME));
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("isValidToken - Token Expired", expiredJwtException);
            return false;
        } catch (JwtException jwtException) {
            log.error("isValidToken - Token Tampered", jwtException);
            return false;
        } catch (NullPointerException npe) {
            log.error("isValidToken - Token is null", npe);
            return false;
        }
    }

    /**
     * 토큰의 만료기간을 지정하는 함수
     * @return Date
     */
    private static Date createExpiredDate() {
        // 토큰의 만료기간은 8시간으로 지정
        Instant now = Instant.now();
        Instant expiryDate = now.plus(Duration.ofHours(8));
        return Date.from(expiryDate);
    }

    /**
     * JWT의 헤더값을 생성해주는 메서드
     */
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", JWT_TYPE);
        header.put("alg", ALGORITHM);
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    /**
     * 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     * @param memberDto 사용자 정보
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(MemberDto memberDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME, memberDto.username());
        claims.put(NICKNAME, memberDto.nickname());
        // fixme: 추후에는 아래 주석 해제
//        claims.put(ROLE, memberDto.roleType().name());
        claims.put(ROLE, "MEMBER");
        return claims;
    }

    /**
     * 토큰을 기반으로 사용자의 권한 정보를 반환받는 메서드
     * @return RoleType : 사용자의 권한
     */
//    public static RoleType getRoleFromToken(String token) {
//        Claims claims = getClaimsFormToken(token);
//        return RoleType.valueOf(claims.get(ROLE).toString());
//    }

    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     * @return Claims : Claims
     */
    private static Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
    }

    /**
     * 토큰을 기반으로 사용자 정보를 반환받는 메서드
     * @return String : 사용자 아이디
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get(USERNAME).toString();
    }

}

