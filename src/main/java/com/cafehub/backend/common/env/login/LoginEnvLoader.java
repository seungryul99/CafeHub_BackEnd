package com.cafehub.backend.common.env.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("login.setting")
public class LoginEnvLoader {
    private final String jwtAccessCookieSetting;
    private final String jwtRefreshCookieSetting;
    private final String jwtRefreshCookieDelSetting;
    private final String frontLoginSuccessUri;
    private final String frontLogoutSuccessUri;
}
