package com.mbti.userauth.user.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    
    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
 
    private Key key;
    
    public TokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
        @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
     }
     
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * jwt 생성
     * @param authentication
     * @return
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
           .map(GrantedAuthority::getAuthority)
           .collect(Collectors.joining(","));
  
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        return Jwts.builder()
           .setSubject(authentication.getName())
           .claim(AUTHORITIES_KEY, authorities)
           .claim("userId", authentication.getName())
           .signWith(key, SignatureAlgorithm.HS512)
           .setExpiration(validity)
           .compact();
     }   

     /**
      * jwt 생성
      * @param userId
      * @return
      */
     public String createToken(String userId) {

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds);
      return Jwts.builder()
         .setSubject(userId)
         .claim(AUTHORITIES_KEY, "")
         .claim("userId", userId)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }   

     /**
      * refresh토큰 생성
      * @param authentication
      * @return
      */
    public String createRefreshToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
           .map(GrantedAuthority::getAuthority)
           .collect(Collectors.joining(","));
  
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        return Jwts.builder()
           .setSubject(authentication.getName())
           .claim(AUTHORITIES_KEY, authorities)
           .claim("userId", authentication.getName())
           .signWith(key, SignatureAlgorithm.HS512)
           .setExpiration(validity)
           .compact();
     }    

     /**
      * refresh토큰 생성
      * @param userId
      * @return
      */
     public String createRefreshToken(String userId) {
      long now = (new Date()).getTime();
      Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);
      return Jwts.builder()
         .setSubject(userId)
         .claim(AUTHORITIES_KEY, "")
         .claim("userId", userId)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }  

     /**
      * 인증정보 반환
      * @param token
      * @return
      */
     public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
  
        Collection<? extends GrantedAuthority> authorities =
           Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());
  
        User principal = new User(claims.getSubject(), "", authorities);
  
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
     }
     
     /**
      * 클레임정보 추출
      * @param token
      * @param claimKey
      * @return
      */
     public String getClaim(String token, String claimKey) {
      String resClaimValue ="";
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

              resClaimValue = claims.get(claimKey).toString();
      

      return resClaimValue;
   }

   /**
    * 토큰정보 검증
    * @param token
    * @return
    */
     public boolean validateToken(String token) {
        try {
           Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
           return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
           logger.info("잘못된 JWT 서명입니다." + e);
        } catch (ExpiredJwtException e) {
           logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
           logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
           logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
     }     

     /**
      * 토큰정보추출
      * @param token
      * @return
      */
     public Map<String, Object> getTokenInfo(String token){
      Map<String, Object> claimMap = null;
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
              .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
              .getBody();

      claimMap = claims;

      return claimMap;
     }

     /**
      * header의 토큰정보 추출
      * @param request
      * @param authHeaderNm
      * @return
      */
     public String resolveToken(HttpServletRequest request, String authHeaderNm) {
      String bearerToken = request.getHeader(authHeaderNm);
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }     
   
   /**
    * String으로 전달받은 header 인증 토큰 정보 추출
    * @param auth
    * @param authHeaderNm
    * @return
    */
   public String resolveTokenString(String auth, String authHeaderNm) {
      String bearerToken = auth;
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }
}
