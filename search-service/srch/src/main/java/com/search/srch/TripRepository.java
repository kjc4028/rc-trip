package com.search.srch;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TripRepository extends MongoRepository<TripEntity, String> {
} 