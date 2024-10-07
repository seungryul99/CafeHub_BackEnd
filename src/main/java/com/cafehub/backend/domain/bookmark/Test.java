package com.cafehub.backend.domain.bookmark;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Test {


    @GetMapping("/api/auth/bookmarks")
    public ResponseEntity<Void> test(){

        log.info("테스트 도착");
        return null;
    }

    @GetMapping("/api/auth/mypage")
    public ResponseEntity<Void> test2(){

        log.info("테스트 도착");
        return null;
    }
}
