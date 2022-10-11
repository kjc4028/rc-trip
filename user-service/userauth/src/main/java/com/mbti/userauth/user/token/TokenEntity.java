package com.mbti.userauth.user.token;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("token")
public class TokenEntity {
    
    private String accessToken;

    private String refreshToken;
}
