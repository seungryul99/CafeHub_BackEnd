package com.cafehub.backend.domain.authInfo.repository;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {


    AuthInfo findByAppId(Long appId);
}
