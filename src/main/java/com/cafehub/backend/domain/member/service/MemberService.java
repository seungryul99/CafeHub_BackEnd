package com.cafehub.backend.domain.member.service;

import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.jwt.JwtProvider;
import com.cafehub.backend.domain.member.jwt.MemberInfoDto;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class MemberService {

    @Value("${kakao.loginUrl}")
    private String kakaoLoginUrl;

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    @Value("${kakao.clientSecret}")
    private String clientSecret;

    private final WebClient webClient;

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    public MemberService(WebClient.Builder webClientBuilder, MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.webClient = webClientBuilder.build();
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String getKakaoLoginPageUrl() {

        return kakaoLoginUrl
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
    }


    public Map<String, String> getKakoResourceServerAccessToken(String authorizationCode) {

        // WebClient를 통해 카카오 서버에 액세스 + 리프레쉬 토큰 요청
        Mono<Map<String, Object>> tokenMono = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                        .with("code", authorizationCode))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});  // 반환값을 Map<String, Object>로 파싱



        // 토큰 받아온 후 로그 찍기
        Map<String, Object> tokenMap = tokenMono.block();
        String accessToken = (String) tokenMap.get("access_token");
        log.info("카카오 인증서버에서 Access Token 받아오기 성공: " + accessToken);


        // 사용자 정보 조회
        String userInfo = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 사용자 정보를 동기적으로 받음


        // JSON을 예쁘게 포맷팅
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String userInfoGson = gson.toJson(gson.fromJson(userInfo, Object.class));
        log.info("카카오 리소스 서버에서 사용자 정보 가져오기 성공: " + userInfoGson);


        // JSON을 Map으로 파싱
        Map<String, Object> userInfoMap = gson.fromJson(userInfo, Map.class);

        // 원하는 필드 추출
        String appId = String.valueOf(userInfoMap.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfoMap.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String email = (String) kakaoAccount.get("email");

        // 로그 출력
        log.info("사용자의 CafeHub <-> Kakao 간 고유 식별 ID : " + appId);
        log.info("사용자 닉네임: " + nickname);
        log.info("사용자 프로필 이미지 URL: " + profileImageUrl);
        log.info("사용자 이메일: " + email);



        if (!memberRepository.existsByAppId(appId)) {
            // 회원가입
            Member newMember = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .appId(appId)
                    .build();

            memberRepository.save(newMember);
            log.info("회원가입 성공");
        }

        MemberInfoDto memberInfoDto = memberRepository.findByAppId(appId);

        // JWT 발급 로직으로 넘어감
        String jwtAccessToken = jwtProvider.createJwtAccessToken(memberInfoDto);
        log.info("JWT ACCESS TOKEN 발급 성공!");

        // 추후 여기에 RefreshToken도 같이 발급해주면 됨
        String jwtRefreshToken = jwtProvider.createJwtRefreshToken(memberInfoDto);
        log.info("JWT REFRESH TOKEN 발급 성공!");


        // JWT와 함께 반환
        Map<String, String> jwtTokens = new HashMap<>();
        jwtTokens.put("jwtAccessToken", jwtAccessToken);
        jwtTokens.put("jwtRefreshToken", jwtRefreshToken);

        return jwtTokens;
    }
}
