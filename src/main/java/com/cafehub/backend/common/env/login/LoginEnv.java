package com.cafehub.backend.common.env.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginEnv {
    private final String jwtAccessCookieSetting;
    private final String jwtRefreshCookieSetting;
    private final String jwtRefreshCookieDelSetting;
    private final String frontLoginSuccessUri;
    private final String frontLogoutSuccessUri;
}
