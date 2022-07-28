package com.mbti.userauth.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    public UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String signup(UserEntity userEntity){
        String procStts = "fail";
        Optional<UserEntity> userOpt =  userRepository.findByUserId(userEntity.getUserId());
        
        if(!userOpt.isPresent()){
            String encPw = passwordEncoder.encode(userEntity.getUserPw());
            userEntity.setUserPw(encPw);
            userRepository.save(userEntity);
            procStts = "succ";
        }

        return procStts;
    }
}
