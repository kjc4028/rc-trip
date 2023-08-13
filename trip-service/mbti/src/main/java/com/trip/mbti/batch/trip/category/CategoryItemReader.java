package com.trip.mbti.batch.trip.category;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.lang.Nullable;

public class CategoryItemReader implements ItemReader<CategoryDto>  {
    private Iterator<CategoryDto> iterator;
    private List<CategoryDto> list;

    public CategoryItemReader(List<CategoryDto> inputList){
        this.iterator = inputList.iterator();
        this.list = inputList;
    }

    @Override
    @Nullable
    public CategoryDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        System.out.println("readtest>>> ");

        return iterator.hasNext() ? iterator.next() : null;
    }


    
}
