package com.trip.mbti.batch.trip.category;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.trip.mbti.rest.category.CategoryEntity;
import com.trip.mbti.rest.category.CategoryService;

import ch.qos.logback.classic.Logger;

public class CategoryItemWriter implements ItemWriter<CategoryDto> {
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    //@Autowired
    private CategoryService categoryService;

    String categoryLevel;

    public CategoryItemWriter(String level, CategoryService categoryService){
        this.categoryLevel = level;
        this.categoryService = categoryService;
    }


    @Override
    public void write(List<? extends CategoryDto> items) throws Exception {
        
        log.info(">>>>>>>>>>batchpoint CategoryItemWriter write");
        log.info(">>>>>>>>>>batchpoint CategoryItemWriter write items " + items.toString());
        for (CategoryDto categoryDto : items) {
            log.info("> " + categoryDto.toString());
            CategoryEntity categoryEntity = categoryDto.toEntity();
            categoryEntity.setLevel(categoryLevel);

            categoryService.save(categoryEntity);
        }
    }


    
}
