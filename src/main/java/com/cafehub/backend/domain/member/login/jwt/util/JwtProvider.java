package com.cafehub.backend.domain.member.login.jwt.util;


import com.cafehub.backend.domain.member.login.jwt.dto.JwtTokenPayloadCreateDTO;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.cafehub.backend.common.constants.CafeHubConstants.ACCESS_TOKEN_EXPIRATION_MS;
import static com.cafehub.backend.common.constants.CafeHubConstants.REFRESH_TOKEN_EXPIRATION_MS;

@Component
public class JwtProvider {

    private final SecretKey secretKey;


    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String createJwtAccessToken(JwtTokenPayloadCreateDTO jwtTokenPayloadCreateDTO) {
        return Jwts.builder()
                .claim("tokenType", "jwt_access")
                .claim("memberId", jwtTokenPayloadCreateDTO.getMemberId())
                .claim("OAuthProvider", jwtTokenPayloadCreateDTO.getProvider())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtRefreshToken(JwtTokenPayloadCreateDTO jwtTokenPayloadCreateDTO) {
        return Jwts.builder()
                .claim("tokenType", "jwt_refresh")
                .claim("memberId", jwtTokenPayloadCreateDTO.getMemberId())
                .claim("OAuthProvider", jwtTokenPayloadCreateDTO.getProvider())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

}
