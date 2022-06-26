package com.trip.mbti.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signup(UserEntity userEntity){
        //TO DO 중복 아이디인지 확인 로직 구현하기
        String encPw = passwordEncoder.encode(userEntity.getUserPw());
        userEntity.setUserPw(encPw);
        userRepository.save(userEntity);
    }
}
