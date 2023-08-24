package com.trip.mbti.batch.trip.tripinfo;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.trip.mbti.rest.trip.TripEntity;
import com.trip.mbti.rest.trip.TripService;

import ch.qos.logback.classic.Logger;

public class TripInfoWriter implements ItemWriter<TripInfoDto>  {

    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    private TripService tripService;
    
    public TripInfoWriter(){
        
        
    }

    public TripInfoWriter(TripService tripService){
        this.tripService = tripService;
        
    }
    
    
    @Override
    public void write(List<? extends TripInfoDto> items) throws Exception {
        log.info(">>>>>>>>>>batchpoint TripInfoDto write");
        log.info(">>>>>>>>>>batchpoint TripInfoDto write items " + items.toString());
        for (TripInfoDto tripInfoDto : items) {
            log.info("> " + tripInfoDto.toString());
            TripEntity tripEntity = tripInfoDto.toEntity();

            tripService.save(tripEntity);
        }
    }
    
}
