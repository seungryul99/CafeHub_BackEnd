package com.cafehub.backend.domain.member.login.jwt.util;


import com.cafehub.backend.domain.member.login.exception.InvalidJwtAccessTokenException;
import com.cafehub.backend.domain.member.login.exception.InvalidJwtRefreshTokenException;
import com.cafehub.backend.domain.member.login.exception.JwtAccessTokenExpiredException;
import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenExpiredException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtValidator {

    private final JwtPayloadReader jwtPayloadReader;
    private final SecretKey secretKey;

    public JwtValidator(JwtPayloadReader jwtPayloadReader, @Value("${spring.jwt.secret}") String secret) {
        this.jwtPayloadReader = jwtPayloadReader;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

//    public boolean validateJwtAccessToken(String token) {
//        try {
//            Jwts.parser()
//                    .verifyWith(secretKey)   // 시크릿 키 세팅
//                    .build()
//                    .parseSignedClaims(token); // 토큰 검증, 여기서 문제가 생기면 return true 로 가지 못함
//
//            // jwt Access Token을 넣은 것인지 검증
//            isAccessToken(token);
//
//            return true;
//        } catch (ExpiredJwtException e){   // ExpiredJwtException -> ClaimJwtException -> JwtException
//            throw new JwtAccessTokenExpiredException();
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new InvalidJwtAccessTokenException();
//        }
//    }

    public void validateJwtAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)   // 시크릿 키 세팅
                    .build()
                    .parseSignedClaims(token); // 토큰 검증

            // tokenType이 access인지 검증
            isAccessToken(token);

        } catch (ExpiredJwtException e){   // ExpiredJwtException -> ClaimJwtException -> JwtException
            throw new JwtAccessTokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAccessTokenException();
        }
    }

    private void isAccessToken(String token){
        if(!jwtPayloadReader.getTokenType(token).equals("jwt_access")) throw new InvalidJwtAccessTokenException();
    }



    public void validateJwtRefreshToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            isRefreshToken(token);

        } catch (ExpiredJwtException e){ // 프론트 쪽에서 다시 로그인 하라는 화면 처리가 필요함
            throw new JwtRefreshTokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtRefreshTokenException();
        }
    }

    private void isRefreshToken(String token){
        if(!jwtPayloadReader.getTokenType(token).equals("jwt_refresh")) throw new InvalidJwtRefreshTokenException();
    }

}
