package com.mbti.userauth.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityWrap = userRepository.findByUserId(username);
        
        UserEntity  userEntity = userEntityWrap.get(); 
        List<GrantedAuthority> grantedAuthorities =  new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return new User(userEntity.getUserId(), userEntity.getUserPw(), grantedAuthorities);
    }
    
}
