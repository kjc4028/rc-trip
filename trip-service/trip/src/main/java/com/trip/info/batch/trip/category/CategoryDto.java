package com.trip.info.batch.trip.category;

import com.trip.info.rest.category.CategoryEntity;

import lombok.Data;

@Data
public class CategoryDto {
    private String code;
    private String name;
    private String rnum;
    private String level;

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .code(code)
                .name(name)
                .rnum(rnum)
                .level(level)
                .build();
    }

}
