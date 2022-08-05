package com.mbti.userauth.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mbti.userauth.common.Message;
import com.mbti.userauth.user.jwt.JwtFilter;
import com.mbti.userauth.user.jwt.TokenProvider;

@RestController
public class UserController {
    
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

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

    @PostMapping(path = "/user/login", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authorize( @RequestBody UserEntity loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getUserPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }    

    @PostMapping(path = "/user/logout")
    public ResponseEntity<Message> logout() {

        Message message = new Message();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpstatus = HttpStatus.OK;
        
        message.setStatus(httpstatus);
        message.setData("logoutSuccess");
        message.setMessage("로그아웃 되었습니다."); 

        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "logout user");
        return new ResponseEntity<Message>(message, httpHeaders, httpstatus);
    } 

    @GetMapping(path = "/user/claim/{claimKey}" , consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> getClaim(HttpServletRequest request, @PathVariable String claimKey) {

        Message message = new Message();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpstatus = HttpStatus.OK;
        System.out.println("kjc----claim");
        System.out.println(claimKey);
        String jwt = tokenProvider.resolveToken(request, AUTHORIZATION_HEADER);
        String claimDate = tokenProvider.getClaim(jwt, claimKey);

        message.setStatus(httpstatus);
        message.setData(claimDate);
        message.setMessage("클레임 조회"); 
        
        return new ResponseEntity<Message>(message, httpHeaders, httpstatus);
    }     

}
