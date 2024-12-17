package com.cafehub.backend.domain.authInfo.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import static com.cafehub.backend.common.constants.CafeHubConstants.KAKAO_OAUTH_PROVIDER_NAME;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    private String provider;

    private String jwtRefreshToken;

    private Date jwtRefreshTokenExpireIn;

    private Long appId;

    @Builder
    private AuthInfo(String provider, String jwtRefreshToken, Date jwtRefreshTokenExpireIn, Long appId) {
        this.provider = provider;
        this.jwtRefreshToken = jwtRefreshToken;
        this.jwtRefreshTokenExpireIn = jwtRefreshTokenExpireIn;
        this.appId = appId;
    }

    public void updateAuthInfoByJwtIssue(String jwtRefreshToken, Date jwtRefreshTokenExpireIn) {

        this.jwtRefreshToken = jwtRefreshToken;
        this.jwtRefreshTokenExpireIn = jwtRefreshTokenExpireIn;
    }

    public void updateJwtRefreshTokenByReIssue(String jwtRefreshToken, Date jwtRefreshTokenExpireIn){

        this.jwtRefreshToken = jwtRefreshToken;
        this.jwtRefreshTokenExpireIn = jwtRefreshTokenExpireIn;
    }

    public void deleteJwtRefreshTokenByLogout(){
        this.jwtRefreshToken = null;
        this.jwtRefreshTokenExpireIn = null;
    }

    public static AuthInfo from(Long appId, String provider){

        return AuthInfo.builder()
                .appId(appId)
                .provider(provider)
                .build();
    }
}
