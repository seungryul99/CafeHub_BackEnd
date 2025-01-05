package com.cafehub.backend.domain.member.login.jwt.util;

import com.cafehub.backend.common.env.jwt.JwtProperties;
import com.cafehub.backend.common.util.JwtThreadLocalStorageManager;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtPayloadCreateDTO;
import com.cafehub.backend.domain.member.repository.RedisRepository;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenManager {

    private final JwtFactory jwtProvider;
    private final RedisRepository redisRepository;
    private final JwtThreadLocalStorageManager jwtThreadLocalStorageManager;
    public Map<String, String> issueJwtTokens(Member member, String provider) {

        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.from(
                member.getId(),
                provider,
                member.getRole()
        );

        String accessToken = jwtProvider.createJwtAccessToken(payload);
        String refreshToken = jwtProvider.createJwtRefreshToken(payload);

        redisRepository.save(member.getId(), refreshToken);

        log.info("JWT 발급 성공");
        
        return Map.of(JWT_ACCESS_TOKEN, accessToken, JWT_REFRESH_TOKEN, refreshToken);
    }

    public Map<String, String> reIssueJwtTokens(String whiteListTokenKey){

        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.from(
                jwtThreadLocalStorageManager.getMemberId(),
                jwtThreadLocalStorageManager.getProvider(),
                jwtThreadLocalStorageManager.getMemberRole()
        );

        String reIssueAccess = jwtProvider.createJwtAccessToken(payload);
        String reIssueRefresh = jwtProvider.createJwtRefreshToken(payload);

        // 이전 블랙리스트 처리 후, 새로 발급한 토큰 화이트 리스트 처리
        redisRepository.delete(whiteListTokenKey);
        redisRepository.save(jwtThreadLocalStorageManager.getMemberId(), reIssueRefresh);

        log.info("JWT 재발급 성공");

        return Map.of(
                JWT_ACCESS_TOKEN, reIssueAccess,
                JWT_REFRESH_TOKEN, reIssueRefresh
        );
    }


    @Component
    static class JwtFactory {

        private final SecretKey secretKey;
        private final JwtProperties jwtProperties;

        public JwtFactory(JwtProperties jwtProperties) {
            this.jwtProperties = jwtProperties;
            secretKey = new SecretKeySpec(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        }

        public String createJwtAccessToken(JwtPayloadCreateDTO payload) {
            return Jwts.builder()
                    .claim("tokenType", "jwt_access")
                    .claim("memberId", payload.getMemberId())
                    .claim("OAuthProvider", payload.getProvider())
                    .claim("role", payload.getRole())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMs()))
                    .signWith(secretKey)
                    .compact();
        }

        public String createJwtRefreshToken(JwtPayloadCreateDTO payload) {
            return Jwts.builder()
                    .claim("tokenType", "jwt_refresh")
                    .claim("memberId", payload.getMemberId())
                    .claim("OAuthProvider", payload.getProvider())
                    .claim("role", payload.getRole())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpirationMs()))
                    .signWith(secretKey)
                    .compact();
        }
    }
}
