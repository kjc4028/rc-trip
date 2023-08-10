package com.trip.mbti.rest.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;



    public List<CategoryEntity> findByCode(CategoryEntity categoryEntity) {
       return categoryRepository.findByCode(categoryEntity); 
    }
    public List<CategoryEntity> findByCodeAndLevel(CategoryEntity categoryEntity) {
       return categoryRepository.findByCodeAndLevel(categoryEntity); 
    }


   
}
