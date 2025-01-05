package com.cafehub.backend.common.env.login;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LoginEnvConfig {

    private final LoginEnvLoader loginEnvLoader;

    @Bean
    public LoginEnv loginEnv(){
        return new LoginEnv(
                loginEnvLoader.getJwtAccessCookieSetting(),
                loginEnvLoader.getJwtRefreshCookieSetting(),
                loginEnvLoader.getJwtRefreshCookieDelSetting(),
                loginEnvLoader.getFrontLoginSuccessUri(),
                loginEnvLoader.getFrontLogoutSuccessUri()
        );
    }
}
