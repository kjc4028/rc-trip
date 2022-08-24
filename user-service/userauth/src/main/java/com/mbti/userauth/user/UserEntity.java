package com.mbti.userauth.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("user")
public class UserEntity {
    
    @Id
    private String _id;

    private String userId;
    
    private String userPw;

}
