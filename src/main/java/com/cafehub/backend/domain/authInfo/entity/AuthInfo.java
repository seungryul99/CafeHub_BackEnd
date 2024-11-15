package com.cafehub.backend.domain.authInfo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInfo {

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

    public void updateAuthInfoByJwtIssue(String jwtRefreshToken, Date expiration) {

        this.jwtRefreshToken = jwtRefreshToken;
        this.jwtRefreshTokenExpireIn = expiration;
    }
}
