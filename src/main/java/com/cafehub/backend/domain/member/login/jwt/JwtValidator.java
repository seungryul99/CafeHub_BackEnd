package com.cafehub.backend.domain.member.login.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtValidator {

    private final SecretKey secretKey;

    public JwtValidator(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public boolean validateJwtAccessToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);

            return true;
        } catch (SignatureException e) {
            log.info("서명 오류");
        } catch (MalformedJwtException e) {
            log.info("유효하지 않은 토큰");
        } catch (ExpiredJwtException e) {
            log.info("만료 된 토큰");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid");
        }
        return false;
    }
}
