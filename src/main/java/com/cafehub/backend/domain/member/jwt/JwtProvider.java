package com.cafehub.backend.domain.member.jwt;


import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;

    private static final long ACEESS_TOKEN_EXPIRATION_MS = 60 * 60 * 1000; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 60 * 60 * 1000 *24; // 24시간


    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String createJwtAccessToken(JwtMemberPayloadDTO jwtMemberPayloadDTO) {
        return Jwts.builder()
                .claim("tokenType", "jwt_access")
                .claim("nickname", jwtMemberPayloadDTO.getNickname())
                .claim("memberId", jwtMemberPayloadDTO.getMemberId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACEESS_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtRefreshToken(JwtMemberPayloadDTO jwtMemberPayloadDTO) {
        return Jwts.builder()
                .claim("tokenType", "jwt_Refresh")
                .claim("memberId", jwtMemberPayloadDTO.getMemberId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

}
