package com.trip.info.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.info.filter.TokenProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 로컬 개발 및 테스트 환경에서만 활성화되는 테스트용 토큰 발급 컨트롤러
 * 중요: @Profile("local") 어노테이션으로 인해 'local' 프로필에서만 Bean으로 등록됩니다.
 */
@Profile("local")
@RestController
@RequestMapping("/trips/local")
public class LocalTestTokenController {

    private static final Logger logger = LoggerFactory.getLogger(LocalTestTokenController.class);
    private final TokenProvider tokenPrivider;

    public LocalTestTokenController(TokenProvider tokenPrivider) {
        this.tokenPrivider = tokenPrivider;
        logger.warn("======================================================");
        logger.warn("주의: 테스트용 토큰 발급 API가 활성화되었습니다. (profile=local)");
        logger.warn("======================================================");
    }

    /**
     * 테스트용 토큰을 동적으로 생성하여 반환합니다.
     * 예: /local/api/token?userId=test-admin&role=ROLE_ADMIN
     */
    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getTestToken(
            @RequestParam(defaultValue = "local-test-user") String userId
            ) {

        String token = tokenPrivider.createToken(userId);

        logger.info("테스트 토큰이 발급되었습니다. User: {}, Role: {}", userId);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}