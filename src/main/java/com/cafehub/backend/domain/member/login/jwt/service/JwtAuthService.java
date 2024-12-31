package com.cafehub.backend.domain.member.login.jwt.service;

import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.entity.Role;
import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenBlockedException;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtPayloadCreateDTO;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import com.cafehub.backend.domain.member.login.jwt.util.JwtProvider;
import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import com.cafehub.backend.domain.member.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cafehub.backend.common.constants.CafeHubConstants.JWT_ACCESS_TOKEN;
import static com.cafehub.backend.common.constants.CafeHubConstants.JWT_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtAuthService {

    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final RedisTemplate<String,String> redisTemplate;

    public Map<String, String> reIssueJwt(String jwtRefreshToken) {

        // 전달 받은 jwt Refresh Token 검증
        jwtValidator.validateJwtRefreshToken(jwtRefreshToken);
        Member member = memberRepository.findById(jwtPayloadReader.getMemberId(jwtRefreshToken)).orElseThrow(MemberNotFoundException::new);

        
        // 화이트리스트에 있는 토큰이 맞는지 체크
        String whiteListRefreshTokenKey = null, whiteListRefreshToken = null;

        // 특정 회원의 토큰 패턴: "members:{memberId}:token:*"
        String memberKeyPattern = "members:" + member.getId() + ":token:*";

        // SCAN 옵션 설정: 해당 패턴과 일치하는 키를 찾음
        ScanOptions options = ScanOptions.scanOptions().match(memberKeyPattern).build();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

        try {
            while (cursor.hasNext()) {
                String tokenKey = new String(cursor.next());
                String token = redisTemplate.opsForValue().get(tokenKey);  // 키에 저장된 토큰값을 가져옴

                System.out.println("토큰 키 : " + tokenKey + " + 토큰 값 : " + token);
                if(token.equals(jwtRefreshToken)){
                    whiteListRefreshTokenKey = tokenKey;
                    whiteListRefreshToken = token;
                    break;
                }
            }
        } finally {
            cursor.close(); // 커서 닫기
        }
        System.out.println("=== 전달된 토큰 ==== , " + jwtRefreshToken);
        System.out.println("======== 레디스 로그 ========= , " + whiteListRefreshToken);
        if(whiteListRefreshToken==null) throw new JwtRefreshTokenBlockedException();

        // 전달 받은 Refresh Token으로 부터 Token 재발급을 위한 정보를 추출함
        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.from(
                jwtPayloadReader.getMemberId(jwtRefreshToken),
                jwtPayloadReader.getProvider(jwtRefreshToken),
                Role.valueOf(jwtPayloadReader.getMemberRole(jwtRefreshToken))
        );


        // 액세스 토큰 재발급
        String reIssueAccess = jwtProvider.createJwtAccessToken(payload);
        
        // 리프레시 토큰 회전
        String reIssueRefresh = jwtProvider.createJwtRefreshToken(payload);

        // 토큰 회전에 사용된 기존의 토큰 화이트 리스트에서 제거
        redisTemplate.delete(whiteListRefreshTokenKey);

        // 새로 발급한 리프레시 토큰 등록
        String memberKey = "members:" + member.getId();
        Long idx = redisTemplate.opsForHash().increment(memberKey, "idx", 1); // idx 값을 증가시킴
        String tokenKey = "members:" + member.getId() + ":token:" + idx; // 고유한 토큰 키
        long ttl = (jwtPayloadReader.getExpiration(jwtRefreshToken).getTime() - System.currentTimeMillis() + 100)/1000;
        redisTemplate.opsForValue().set(tokenKey,reIssueRefresh,ttl, TimeUnit.SECONDS);


        return  Map.of(
                JWT_ACCESS_TOKEN, reIssueAccess,
                JWT_REFRESH_TOKEN, reIssueRefresh
        );
    }
}
