package com.cafehub.backend.domain.member.login.jwt;


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

    private static final long ACCESS_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 6; // 1000ms = 1초, 1초 *60 *60 *12 = 6시간
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24시간


    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String createJwtAccessToken(JwtTokenPayloadCreateDTO jwtTokenPayloadCreateDTO) {
        return Jwts.builder()
                .claim("tokenType", "jwt_access")
                .claim("nickname", jwtTokenPayloadCreateDTO.getNickname())
                .claim("memberId", jwtTokenPayloadCreateDTO.getMemberId())
                .claim("OAuthProvider", jwtTokenPayloadCreateDTO.getProvider())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtRefreshToken(JwtTokenPayloadCreateDTO jwtTokenPayloadCreateDTO) {
        return Jwts.builder()
                .claim("tokenType", "jwt_Refresh")
                .claim("memberId", jwtTokenPayloadCreateDTO.getMemberId())
                .claim("OAuthProvider", jwtTokenPayloadCreateDTO.getProvider())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

}