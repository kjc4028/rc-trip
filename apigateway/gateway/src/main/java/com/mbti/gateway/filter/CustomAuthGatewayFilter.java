package com.mbti.gateway.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CustomAuthGatewayFilter extends AbstractGatewayFilterFactory<CustomAuthGatewayFilter.Config>{
    


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
            System.out.println("filter start >>>>>>>");
            ServerHttpRequest request = exchange.getRequest();
            System.out.println(request.getHeaders());
            // Request Header 에 token 이 존재하지 않을 때
            if(!request.getHeaders().containsKey(AUTHORIZATION_HEADER)){
                System.out.println("filter 토큰 미존재 >>>>>>>");
                return handleUnAuthorized(exchange); // 401 Error
            }
            
            // Request Header 에서 token 문자열 받아오기
            List<String> token = request.getHeaders().get(AUTHORIZATION_HEADER);
            String tokenString = Objects.requireNonNull(token).get(0);
            System.out.println(token);
            System.out.println(tokenString);


            // 토큰 검증
            //if(!tokenString.equals("A.B.C")) {
            if(!tokenProvider.validateToken(resolveToken(tokenString))) {
                System.out.println("filter 토큰 불일치 >>>>>>>");
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

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
           return bearerToken.substring(7);
        }
        return null;
     }        
}
