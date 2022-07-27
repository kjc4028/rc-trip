package com.mbti.gateway.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CustomAuthGatewayFilter extends AbstractGatewayFilterFactory<CustomAuthGatewayFilter.Config>{
    
    public CustomAuthGatewayFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            System.out.println("filter start >>>>>>>");
            ServerHttpRequest request = exchange.getRequest();

            // Request Header 에 token 이 존재하지 않을 때
            if(!request.getHeaders().containsKey("token")){
                return handleUnAuthorized(exchange); // 401 Error
            }

            // Request Header 에서 token 문자열 받아오기
            List<String> token = request.getHeaders().get("token");
            String tokenString = Objects.requireNonNull(token).get(0);

            // 토큰 검증
            if(!tokenString.equals("A.B.C")) {
                return handleUnAuthorized(exchange); // 토큰이 일치하지 않을 때
            }
            System.out.println("filter end >>>>>>>");
            return chain.filter(exchange); // 토큰이 일치할 때

        });
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }    
}
