package com.trip.mbti.rest.category;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {

    List<CategoryEntity> findByCode(String code);
    
    List<CategoryEntity> findByCodeAndLevel(String code, String level);
    
    List<CategoryEntity> findByCodeStartsWithAndLevel(String code, String level);
    
    List<CategoryEntity> findByLevel(String level);

    
}
