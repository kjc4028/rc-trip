package com.trip.info.batch.trip.tripDtlInfo;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.trip.info.core.trip.TripDto;
import com.trip.info.core.trip.TripEntity;
import com.trip.info.core.trip.TripService;

import ch.qos.logback.classic.Logger;

public class TripDtlInfoWriter implements ItemWriter<TripDto>  {

    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    private TripService tripService;
    
    public TripDtlInfoWriter(){
        
        
    }

    public TripDtlInfoWriter(TripService tripService){
        this.tripService = tripService;
        
    }
    
    
    @Override
    public void write(List<? extends TripDto> items) throws Exception {
        log.info(">>>>>>>>>>batchpoint TripInfoDto write");
        log.info(">>>>>>>>>>batchpoint TripInfoDto write items " + items.toString());
        for (TripDto tripDto : items) {
            log.info("> " + tripDto.toString());
            TripEntity tripEntity = tripDto.toEntity();

            tripService.save(tripEntity);
        }
    }
    
}
