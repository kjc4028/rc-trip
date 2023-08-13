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



    public List<CategoryEntity> findByCode(String code) {
       return categoryRepository.findByCode(code); 
    }

    public List<CategoryEntity> findByCodeAndLevel(String code, String level) {
       return categoryRepository.findByCodeAndLevel(code, level); 
    }

    public List<CategoryEntity> findByCodeStartsWithAndLevel(String code, String level) {
       return categoryRepository.findByCodeStartsWithAndLevel(code, level); 
    }

    public List<CategoryEntity> findByLevel(String level) {
      return categoryRepository.findByLevel(level); 
   }
 
   public List<CategoryEntity> findAll() {
      return categoryRepository.findAll(); 
   }

    public void save(CategoryEntity categoryEntity){
      categoryRepository.save(categoryEntity);
    }

    public void deleteAll(){
      categoryRepository.deleteAll();
    }

   
}
