package com.trip.mbti.batch.trip.category;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.lang.Nullable;

public class CategoryItemReader implements ItemReader<CategoryVo>  {
    private Iterator<CategoryVo> iterator;

    public CategoryItemReader(List<CategoryVo> inputList){
        this.iterator = inputList.iterator();
    }

    @Override
    @Nullable
    public CategoryVo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return iterator.hasNext() ? iterator.next() : null;
    }


    
}
