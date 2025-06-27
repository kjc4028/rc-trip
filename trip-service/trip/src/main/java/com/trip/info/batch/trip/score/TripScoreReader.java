package com.trip.info.batch.trip.score;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.trip.info.rest.trip.TripDto;
import com.trip.info.rest.trip.TripEntity;
import com.trip.info.rest.trip.TripService;

import ch.qos.logback.classic.Logger;

public class TripScoreReader implements ItemReader<TripDto> {
private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private TripService tripService;

    public TripScoreReader(TripService tripService){
        // this.tripService = tripService;
    }

    @Override
    public TripDto read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Page<TripEntity> page = tripService.findAllTripPage(1, 10);
        
        List<TripDto> list = new ArrayList<>();

        for (TripEntity tripEntity : page.toList()) {
            TripDto tripDto = new TripDto();
            tripDto = tripDto.from(tripEntity);
            list.add(tripDto);
            log.info(">>> listtest " + tripDto.toString());
            log.info(">>> tripEntity " + tripEntity.toString());
        }
        return list.iterator().hasNext() ? list.iterator().next() : null;
        
    }
    
}
