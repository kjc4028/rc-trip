package com.mbti.userauth.user.redis;

import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<UserRedisEntity, String>{
    
}
