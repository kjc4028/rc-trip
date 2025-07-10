package com.trip.info.batch.trip.score;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.trip.info.core.trip.TripDto;
import com.trip.info.core.trip.TripEntity;
import com.trip.info.core.trip.TripService;

import ch.qos.logback.classic.Logger;

public class TripScoreReader implements ItemReader<TripDto> {
private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    private TripService tripService;

    private List<TripDto> list = new ArrayList<>();
    private int currentIndex = 0;

    public TripScoreReader(TripService tripService){
        this.tripService = tripService;
        List<TripEntity> dataList = tripService.findByAllScoreNotExistsOrNullOrEmpty();
        if(dataList != null){
            for (TripEntity tripEntity : dataList) {
                this.list.add(new TripDto().from(tripEntity));
            }
        }        
    }

    @Override
    public TripDto read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (currentIndex < list.size()) {
            return list.get(currentIndex++);
        } else {
            // 리스트의 모든 아이템을 다 읽었으면 null을 반환하여 읽기 종료를 알립니다.
            currentIndex = 0; // 필요하다면 다음 Step 실행을 위해 초기화
            return null;
        }
    }
    
}
