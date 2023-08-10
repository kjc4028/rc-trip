package com.trip.mbti.rest.category;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {

    List<CategoryEntity> findByCode(CategoryEntity categoryEntity);
    
    List<CategoryEntity> findByCodeAndLevel(CategoryEntity categoryEntity);
    

}
