package com.mbti.userauth.user.redis;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RedisHash("userInfo")
@NoArgsConstructor
@AllArgsConstructor
public class UserRedisEntity{

    @Id
    private String userId;

    private String accessToken;

    private String refreshToken;

}
