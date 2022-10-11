package com.mbti.userauth.user.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<TokenEntity, String> {
    
    TokenEntity findByAccessToken(String accesstoken);

    void deleteByAccessToken(String accesstoken);
}


