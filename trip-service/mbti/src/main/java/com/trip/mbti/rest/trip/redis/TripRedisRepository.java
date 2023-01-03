package com.trip.mbti.rest.trip.redis;

import org.springframework.data.repository.CrudRepository;

public interface TripRedisRepository extends CrudRepository<TripRedisEntity, String>{
    
}
