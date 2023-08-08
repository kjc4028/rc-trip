package com.trip.mbti.batch.trip.category;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class CategoryItemWriter implements ItemWriter<CategoryVo> {
    
    @Override
    public void write(List<? extends CategoryVo> items) throws Exception {
        


        System.out.println("writetest>>> ");
        System.out.println(">> " + items.toString());

        for (CategoryVo categoryVo : items) {
            System.out.println("> " + categoryVo.toString());
        }
    }


    
}
