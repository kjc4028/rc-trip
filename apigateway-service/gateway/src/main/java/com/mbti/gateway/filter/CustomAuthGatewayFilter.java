package com.mbti.gateway.filter;

import java.util.List;
import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import ch.qos.logback.classic.Logger;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthGatewayFilter extends AbstractGatewayFilterFactory<CustomAuthGatewayFilter.Config>{
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    public CustomAuthGatewayFilter() {
        super(Config.class);
    }

    public static class Config {

    } 

    // @Autowired
    // private TokenProvider tokenProvider;
    
    private TokenProvider tokenProvider;

    @Autowired	// 이 경우 생략 가능(생성자가 하나라)
    public void TokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("filter start >>>>>>>");
            ServerHttpRequest request = exchange.getRequest();
            log.info(""+request.getHeaders());
            // Request Header 에 token 이 존재하지 않을 때
            if(!request.getHeaders().containsKey(AUTHORIZATION_HEADER)){
                log.info("filter 토큰 미존재 >>>>>>>");
                return handleUnAuthorized(exchange);
            }
            
            // Request Header 에서 token 문자열 받아오기
            List<String> token = request.getHeaders().get(AUTHORIZATION_HEADER);
            String tokenString = Objects.requireNonNull(token).get(0);
            log.info(""+token);
            log.info(tokenString);


            // 토큰 검증
            String tokenValidRsCd = tokenProvider.validateToken(resolveToken(tokenString));
            if(!"101".equals(tokenValidRsCd)) {
                log.info("filter 토큰 불일치 >>>>>>>");
                if("103".equals(tokenValidRsCd)){
                    //토큰만료 리프레시토큰 가져와 토큰 재발급 로직 필요
                    log.info("토큰만료 리프레시토큰 가져와 토큰 재발급 로직 필요 >>>>>>>");
                    return handleTokenExpire(exchange);
                } else {
                    return handleUnAuthorized(exchange); // 토큰이 일치하지 않을 때
                }
            }       
            log.info("filter end >>>>>>>");
            return chain.filter(exchange); // 토큰이 일치할 때

        });
    }

    /**
     * 인증불가 상태 반환
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
     * @param exchange
     * @return
     */
    private Mono<Void> handleTokenExpire(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }    

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
           return bearerToken.substring(7);
        }
        return null;
     }        
}
