package com.mbti.userauth.user.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
 
    @Autowired
    private TokenProvider tokenProvider;
 
    public JwtFilter(TokenProvider tokenProvider) {
       this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                String jwt = tokenProvider.resolveToken(httpServletRequest,AUTHORIZATION_HEADER);
                String requestURI = httpServletRequest.getRequestURI();
                boolean jwtValid = tokenProvider.validateToken(jwt, request);
                String expired = (String) request.getAttribute("expired");
                if (StringUtils.hasText(jwt) && jwtValid && expired == null) {
                   Authentication authentication = tokenProvider.getAuthentication(jwt);
                   SecurityContextHolder.getContext().setAuthentication(authentication);
                   logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                } else {
                   logger.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                   if(expired != null){
                     logger.info("토큰 만료");
                     //httpServletResponse.sendError(999,"expired");
                     httpServletResponse.setStatus(999);
                   }
                }
          
                chain.doFilter(request, httpServletResponse);        
    }
    
   //  private String resolveToken(HttpServletRequest request) {
   //      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
   //      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
   //         return bearerToken.substring(7);
   //      }
   //      return null;
   //   }    
}
