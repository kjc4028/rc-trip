package com.mbti.userauth;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mbti.userauth.user.UserEntity;
import com.mbti.userauth.user.UserService;
import com.mbti.userauth.user.jwt.JwtFilter;
import com.mbti.userauth.user.jwt.TokenProvider;
import com.mbti.userauth.user.redis.UserRedisEntity;
import com.mbti.userauth.user.redis.UserRedisRepository;
import com.mbti.userauth.user.token.TokenEntity;
import com.mbti.userauth.user.token.TokenService;

import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SpringBootTest
//@ActiveProfiles("test")
@EnableCaching
class UserauthApplicationTests {

	private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
	public String testJwt = "";
	public String testRefreshToken = "";
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRedisRepository userRedisRepository;
	
	@BeforeEach
	void login(){
		UserEntity loginDto = new UserEntity();

		loginDto.setUserId("q");

		loginDto.setUserPw("q");

		UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getUserPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        String refreshJwt = tokenProvider.createRefreshToken(authentication);
		
		testJwt = jwt;
		testRefreshToken = refreshJwt;

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setAccessToken(jwt);
        tokenEntity.setRefreshToken(refreshJwt);
        tokenService.saveToken(tokenEntity);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

		List<String> headerList = new ArrayList<>();
		headerList = httpHeaders.get(JwtFilter.AUTHORIZATION_HEADER);
		log.info("jwt headerList-1: " + headerList);

        UserRedisEntity userRedisEntity = new UserRedisEntity();
        userRedisEntity.setUserId(loginDto.getUserId());
		userRedisEntity.setAccessToken(jwt);
        userRedisEntity.setRefreshToken(refreshJwt);

        userRedisRepository.save(userRedisEntity);
	}

	@Test
	void redisTokenGetAndDelete(){
		log.info("testJwt: " + testJwt);
		log.info("testRefreshToken: " + testRefreshToken);

		boolean jwtValid = tokenProvider.validateToken(testJwt);
		boolean refreshTokenValid = tokenProvider.validateToken(testRefreshToken);
		
		log.info("jwtValid : " + jwtValid);
		log.info("refreshTokenValid : " + refreshTokenValid);

		String tokenUserId = tokenProvider.getClaim(testJwt, "userId");
		log.info("tokenUserId " + tokenUserId);

		log.info(tokenProvider.getTokenInfo(testJwt).toString());

		Optional<UserRedisEntity> userRedisEntity = userRedisRepository.findById(tokenUserId);

		log.info("getRedisEntity " + userRedisEntity.get().getAccessToken());
		log.info("getRedisEntity " + userRedisEntity.get().getRefreshToken());
		
		userRedisRepository.deleteById(tokenUserId);
		
		Optional<UserRedisEntity> deleteAfterUserRedisEntity = userRedisRepository.findById(tokenUserId);
		
		deleteAfterUserRedisEntity = userRedisRepository.findById(tokenUserId);
		
		assertTrue("refreshToken delete check", !deleteAfterUserRedisEntity.isPresent());
	
	}


	//@Test
	void contextLoads() {
	}

}
