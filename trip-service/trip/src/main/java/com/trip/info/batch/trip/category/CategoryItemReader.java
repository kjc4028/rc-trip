package com.trip.info.batch.trip.category;

import java.util.Iterator;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.lang.Nullable;

import ch.qos.logback.classic.Logger;

public class CategoryItemReader implements ItemReader<CategoryDto>  {
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    private Iterator<CategoryDto> iterator;
    private List<CategoryDto> list;

    public CategoryItemReader(List<CategoryDto> inputList){
        this.iterator = inputList.iterator();
        this.list = inputList;
    }

    @Override
    @Nullable
    public CategoryDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("readtest>>> ");
       
        return iterator.hasNext() ? iterator.next() : null;
    }


    
}
