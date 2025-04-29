package com.trip.info.rest.category;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document("category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity{
    

    private String code;
    private String name;
    private String rnum;
    private String level;



}
