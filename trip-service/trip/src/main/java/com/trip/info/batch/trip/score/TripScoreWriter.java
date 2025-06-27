package com.trip.info.batch.trip.score;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.trip.info.batch.trip.tripinfo.TripInfoDto;
import com.trip.info.rest.trip.TripDto;
import com.trip.info.rest.trip.TripEntity;
import com.trip.info.rest.trip.TripService;

import ch.qos.logback.classic.Logger;

public class TripScoreWriter implements ItemWriter<TripDto> {
    
private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    TripService tripService;

    public TripScoreWriter(){

    }

    @Override
    public void write(List<? extends TripDto> items) throws Exception {
        // log.info(">>>>>>>>>>batchpoint TripDto score write");
        // log.info(">>>>>>>>>>batchpoint TripDto score write items " + items.toString());
        // for (TripDto tripDto : items) {
        //     log.info("> " + tripDto.toString());
        //     TripEntity tripEntity = tripDto.toEntity();
        //     tripService.save(tripEntity);
        // }
    }

    
    
}
