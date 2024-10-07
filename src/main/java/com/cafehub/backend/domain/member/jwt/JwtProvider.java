package com.cafehub.backend.domain.member.jwt;


import com.cafehub.backend.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    /**
     * SecretKey 객체는 Java의 암호화 API (JCA - Java Cryptography Architecture)에서 사용하는 표준 인터페이스입니다.
     * HMAC, AES 등 다양한 암호화 알고리즘에서 서명 및 암호화를 쉽게 처리할 수 있도록 제공됩니다.
     * String은 암호화에 적합한 데이터 구조가 아니며, 안전한 서명 작업을 수행하는데 불편함이 있을 수 있습니다.
     */
    private final SecretKey secretKey;

    private static final long ACEESS_TOKEN_EXPIRATION_MS = 60 * 60 * 1000; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 60 * 60 * 1000 *24; // 24시간


    /**
     * SecretKeySpec은 Java Cryptography Extension (JCE)에서 제공하는 클래스로,
     * 암호화에 사용할 수 있는 SecretKey 객체를 생성하는 데 사용됩니다.
     * 첫 번째 파라미터로 시크릿 키 값의 바이트 배열을 전달하고, 두 번째 파라미터로 알고리즘 이름을 전달합니다.
     *
     * 첫 번째 파라미터에서 secret 문자열을 바이트 배열로 변환.
     * StandardCharsets.UTF_8은 변환 시 UTF-8 인코딩 방식을 사용하여, 문자열을 바이트 배열로 변환합니다.
     * 이 바이트 배열이 바로 SecretKey 객체를 생성하는데 사용됩니다.
     *
     * Jwts.SIG.HS256.key().build().getAlgorithm()은 HMAC-SHA256 (HS256) 서명 알고리즘을 지정하는 코드입니다.
     * **HS256**은 HMAC (해시 기반 메시지 인증 코드) 방식 중 하나로, SHA-256 해시 함수와 결합되어 사용됩니다.
     *
     *
     * 전체 과정:
     * JWT 서명에 사용될 시크릿 키를 설정 파일로부터 읽어와 문자열로 받음.
     * UTF-8 인코딩을 사용해 시크릿 문자열을 바이트 배열로 변환.
     * 변환된 바이트 배열과 HMAC-SHA256 (HS256) 알고리즘을 사용해 SecretKey 객체를 생성.
     * 이 SecretKey 객체는 JWT를 생성하거나 검증할 때 서명과 검증 작업을 처리하는 데 사용됩니다.
     */
    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    /**
     * JWT 라이브러리를 통한 JWT 발급
     */
    public String createJwtAccessToken(MemberInfoDto memberInfoDto) {
        return Jwts.builder()
                .claim("tokenType", "jwt_access")
                .claim("nickname", memberInfoDto.getNickname())
                .claim("memberId", memberInfoDto.getMemberId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACEESS_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtRefreshToken(MemberInfoDto memberInfoDto) {
        return Jwts.builder()
                .claim("tokenType", "jwt_Refresh")
                .claim("memberId", memberInfoDto.getMemberId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }




}
