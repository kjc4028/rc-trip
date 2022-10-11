package com.mbti.userauth.user.token;

import org.antlr.runtime.TokenRewriteStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {
    
    @Autowired
    private TokenRepository tokenRepository;

    public void saveToken(TokenEntity tokenEntity){
        tokenRepository.save(tokenEntity);
    }

    public TokenEntity findByAccessToken(String accessToken){
        return tokenRepository.findByAccessToken(accessToken);
    }

    public void deleteByAccessToken(String accessToken){
        tokenRepository.deleteByAccessToken(accessToken);
    }

}
