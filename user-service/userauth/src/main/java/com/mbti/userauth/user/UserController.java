package com.mbti.userauth.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mbti.userauth.common.Message;
import com.mbti.userauth.user.jwt.JwtFilter;
import com.mbti.userauth.user.jwt.TokenProvider;
import com.mbti.userauth.user.redis.UserRedisEntity;
import com.mbti.userauth.user.redis.UserRedisRepository;
import com.mbti.userauth.user.token.TokenEntity;
import com.mbti.userauth.user.token.TokenService;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

/**
 * 회원정보 및 로그인 controller
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private TokenService tokenService;

    private final TripServiceClient tripServiceClient;

    @Autowired
    private UserRedisRepository userRedisRepository;

    /**
     * 회원가입 요청
     */
    @PostMapping(path = "/user/signup", consumes= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Message> signup(@RequestBody UserEntity userEntity){
        Message message = new Message();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpstatus = HttpStatus.OK;
        
        if(userEntity.getUserId().isEmpty() || userEntity.getUserPw().isEmpty()){
            message.setStatus(httpstatus);
            message.setData("joinFail");
            message.setMessage("아이디와 비밀번호를 입력해주십시오.");    

            return new ResponseEntity<Message>(message, httpHeaders ,httpstatus);
        }

        String signupRs = userService.signup(userEntity);
        if(signupRs.equals("succ")){
            message.setStatus(httpstatus);
            message.setData("joinSucc");
            message.setMessage("회원가입이 정상적으로 되었습니다."); 
        } else {
            message.setStatus(httpstatus);
            message.setData("joinFail");
            message.setMessage("해당 아이디는 이미 등록된 아이디 입니다.");            
        }
   
        return new ResponseEntity<Message>(message, httpHeaders ,httpstatus);
    }

    /*
     * 로그인 요청
     */
    @PostMapping(path = "/user/login", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authorize( @RequestBody UserEntity loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getUserPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        String refreshJwt = tokenProvider.createRefreshToken(authentication);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setAccessToken(jwt);
        tokenEntity.setRefreshToken(refreshJwt);
        tokenService.saveToken(tokenEntity);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        UserRedisEntity userRedisEntity = new UserRedisEntity();
        userRedisEntity.setUserId(loginDto.getUserId());
        userRedisEntity.setAccessToken(jwt);
        userRedisEntity.setRefreshToken(refreshJwt);

        userRedisRepository.save(userRedisEntity);
        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }    

    /*
     * 로그아웃 요청
     */
    @PostMapping(path = "/user/logout")
    public ResponseEntity<Message> logout(HttpServletRequest request) {

        Message message = new Message();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpstatus = HttpStatus.OK;
        
        String jwt = tokenProvider.resolveTokenString(request.getHeader("Authorization"), AUTHORIZATION_HEADER);
        TokenEntity tokenEntity = tokenService.findByAccessToken(jwt);

        if(tokenEntity != null){
            tokenService.deleteByAccessToken(tokenEntity.getAccessToken());
        }

        message.setStatus(httpstatus);
        message.setData("logoutSuccess");
        message.setMessage("로그아웃 되었습니다."); 

        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "logout user");
        return new ResponseEntity<Message>(message, httpHeaders, httpstatus);
    } 

    /*
     * 클레임 조회 요청
     */
    @GetMapping(path = "/user/claim/{claimKey}")
    public ResponseEntity<Message> getClaim(HttpServletRequest request, @PathVariable String claimKey) {

        Message message = new Message();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpstatus = HttpStatus.OK;
        //String jwt = tokenProvider.resolveToken(request, AUTHORIZATION_HEADER);
        String jwt = tokenProvider.resolveTokenString(request.getHeader("Authorization"), AUTHORIZATION_HEADER);
        

        String claimData = tokenProvider.getClaim(jwt, claimKey);

        message.setStatus(httpstatus);
        message.setData(claimData);
        message.setMessage("클레임 조회"); 
        
        return new ResponseEntity<Message>(message, httpHeaders, httpstatus);
    }  
    
    /*회원탈퇴 요청 */
    @DeleteMapping(path = "/user")
    public ResponseEntity<Message> userDelete(HttpServletRequest request){
        Message message = new Message();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpstatus = HttpStatus.OK;

        String jwt = tokenProvider.resolveTokenString(request.getHeader("Authorization"), AUTHORIZATION_HEADER);
        String claimDataUserId = tokenProvider.getClaim(jwt, "userId");
        TripDto tripDto = new TripDto();
        tripDto.setRegUserId(claimDataUserId);
        tripServiceClient.tripDeleteBuRegUserId(tripDto);
        userService.deleteUser(claimDataUserId);

        message.setStatus(httpstatus);
        message.setData("회원탈퇴 완료");
        message.setMessage("회원탈퇴 완료. 작성한 게시글도 삭제됩니다."); 
        
        return new ResponseEntity<Message>(message, httpHeaders, httpstatus);
    }

    /*
     * token refresh 요청
     */
    @PostMapping(path = "/user/tokenRefresh", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> tokenRefresh( @RequestBody UserEntity loginDto) {
       //TODO pw없이 재발급 가능하도록 구현 필요
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getUserPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        Optional<UserRedisEntity> findUserRedisEntity = userRedisRepository.findById(loginDto.getUserId());

        String jwt = tokenProvider.createToken(authentication);
        String refreshJwt = tokenProvider.createRefreshToken(authentication);

        //리프레시토큰 만료여부

        if(findUserRedisEntity.isPresent()){
            log.info(">>>>>>>1");
            if(tokenProvider.validateToken(findUserRedisEntity.get().getRefreshToken())){
                findUserRedisEntity.get().setAccessToken(jwt);
                userRedisRepository.save(findUserRedisEntity.get());
                log.info(">>>>>>>2");
            } else {
                UserRedisEntity userRedisEntity = new UserRedisEntity();
                userRedisEntity.setUserId(loginDto.getUserId());
                userRedisEntity.setAccessToken(jwt);
                userRedisEntity.setRefreshToken(refreshJwt);
        
                userRedisRepository.save(userRedisEntity);
                log.info(">>>>>>>3");
            }

        } else {
            UserRedisEntity userRedisEntity = new UserRedisEntity();
            userRedisEntity.setUserId(loginDto.getUserId());
            userRedisEntity.setAccessToken(jwt);
            userRedisEntity.setRefreshToken(refreshJwt);
    
            userRedisRepository.save(userRedisEntity);
            log.info(">>>>>>>4");
        }
    
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }    

}
