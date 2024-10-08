package com.cafehub.backend.domain.member.jwt;


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
            // 토큰의 서명 및 유효성을 검증하고 Claims(페이로드)를 추출
            Claims claims = Jwts.
                    parser()
                    .setSigningKey(secretKey) // 비밀 키를 설정
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            log.info(String.valueOf(claims));

            // 여기에서 claims로 추가적인 검증 가능 (예: 토큰의 만료 시간)
            return true;
        } catch (SignatureException e) {
            log.info("서명 오류");
        } catch (MalformedJwtException e) {
            log.info("유효하지 않은 토큰");
        } catch (ExpiredJwtException e) {
            log.info("만료된 않은 토큰");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid");
        }
        return false;
    }
}
