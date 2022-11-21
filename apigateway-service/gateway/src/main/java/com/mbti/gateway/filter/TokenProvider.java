package com.mbti.gateway.filter;

import java.security.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final String secret;

 
    private Key key;
    
    public TokenProvider(
        @Value("${jwt.secret}") String secret) {
        this.secret = secret;
     }
     
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


  
     public String validateToken(String token) {
      String rsCode = "";  
      try {
           Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
           return "101";
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
           logger.info("잘못된 JWT 서명입니다." + e);
           rsCode = "102";
        } catch (ExpiredJwtException e) {
           logger.info("만료된 JWT 토큰입니다.");
           rsCode ="103";
        } catch (UnsupportedJwtException e) {
           logger.info("지원되지 않는 JWT 토큰입니다.");
           rsCode = "104";
        } catch (IllegalArgumentException e) {
           logger.info("JWT 토큰이 잘못되었습니다.");
           rsCode = "105";
        }
        return rsCode;
     }     
}
