package com.recipia.member.config.jwt;

import com.recipia.member.config.dto.TokenMemberInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 관련 토큰 Util
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TokenUtils {


    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
    // jwtSecretKey를 바이트 배열로 변환하고, 이를 사용하여 HMAC-SHA256 알고리즘에 사용할 키를 생성한다.
    private static final Key key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    private static final String JWT_TYPE = "JWT";
    private static final String ALGORITHM = "HS256";
    private static final String MEMBER_ID = "memberId";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String ROLE = "role";

    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 30 * 60; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 60 * 60 * 24 * 30 * 6; // 6개월

    /**
     * 사용자 pk를 기준으로 Access Token을 발급하여 반환해 준다.
     */
    public static String generateAccessToken(TokenMemberInfoDto tokenMemberInfoDto) {
        Pair<String, LocalDateTime> jwtPair = generateToken(tokenMemberInfoDto, ACCESS_TOKEN_EXPIRATION_SECONDS, ACCESS_TOKEN_TYPE);
        return jwtPair.getFirst();
    }

    /**
     * 사용자 pk를 기준으로 Refresh Token을 발급하여 반환해 준다.
     * 이때 Refresh Token은 DB에 저장해야 한다.
     */
    public static Pair<String, LocalDateTime> generateRefreshToken(TokenMemberInfoDto tokenMemberInfoDto) {
        return generateToken(tokenMemberInfoDto, REFRESH_TOKEN_EXPIRATION_SECONDS, REFRESH_TOKEN_TYPE);
    }


    /**
     * Pair라는 객체는 두 개의 연관된 값을 함께 그룹화하는데 사용
     * org.springframework.data.util.Pair 사용
     */
    private static Pair<String, LocalDateTime> generateToken(TokenMemberInfoDto tokenMemberInfoDto, long expirationSeconds, String tokenType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(expirationSeconds);

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(tokenMemberInfoDto, tokenType))
                .setSubject(String.valueOf(tokenMemberInfoDto.email()))
                .setIssuer("Recipia")
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256);

        log.info("generateJwtToken - Token generated for user email: " + tokenMemberInfoDto.email());
        return Pair.of(builder.compact(), expiryDate);
    }

    public static Long getMemberIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(MEMBER_ID, Long.class);
    }

    public static String getNicknameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(NICKNAME, String.class);
    }

    public static String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(EMAIL, String.class);
    }

    public static String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(ROLE, String.class);
    }

    /**
     * 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     * @param tokenMemberInfoDto 사용자 정보
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(TokenMemberInfoDto tokenMemberInfoDto, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(MEMBER_ID, tokenMemberInfoDto.id());
        claims.put(EMAIL, tokenMemberInfoDto.email());
        claims.put(NICKNAME, tokenMemberInfoDto.nickname());
        claims.put("type", tokenType); // Token 종류를 저장
        claims.put(ROLE, tokenMemberInfoDto.roleType().name());
        return claims;
    }

    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     * @return Claims : Claims
     */
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
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
}

