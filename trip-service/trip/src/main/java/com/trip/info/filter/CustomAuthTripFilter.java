package com.trip.info.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;

import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthTripFilter extends OncePerRequestFilter {

    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static final String AUTHORIZATION_HEADER = "Authorization";

    // @Autowired
    // private TokenProvider tokenProvider;

    private TokenProvider tokenProvider;

    @Autowired // 이 경우 생략 가능(생성자가 하나라)
    public void TokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludeUrls = Arrays.asList(
            "/trips/local/token" // 로컬 테스트용 토큰 발급 API
    );

    /**
     * 인증불가 상태 반환
     * 
     * @param exchange
     * @return
     */
    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.BAD_REQUEST);

        return response.setComplete();
    }

    /**
     * 토큰만료 상태 반환
     * 
     * @param exchange
     * @return
     */
    private Mono<Void> handleTokenExpire(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.setRawStatusCode(999);
        return response.setComplete();

    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("filter start >>>>>>>");
        String token = resolveToken(request); // 헤더에서 토큰 추출
        log.info("" + token);

        String requestURI = request.getRequestURI();

        // 필터 제외할 URL 목록과 현재 요청 URL을 비교
        boolean isExcludable = excludeUrls.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));

        // 필터 제외할 URL인 경우, 필터의 핵심 로직을 건너뛰고 바로 다음 필터로 이동
        if (isExcludable) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (token != null) {
                // 토큰 검증 성공 시
                String tokenValidRsCd = tokenProvider.validateToken(token);
                // Claims claims = jwtTokenValidator.validateTokenAndGetClaims(token);

                if (!"101".equals(tokenValidRsCd)) {
                    log.info("filter 토큰 불일치 >>>>>>>" + tokenValidRsCd);
                    if ("103".equals(tokenValidRsCd)) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
                        return;
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 일치하지 않습니다.");
                        return;
                    }
                }

            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 존재하지 않습니다.");
                return;
            }
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            return;
        }
        log.info("filter end >>>>>>>");
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달

    }
}
