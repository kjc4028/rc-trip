package com.trip.info.batch.trip.score;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.trip.info.rest.score.ScoreService;
import com.trip.info.rest.trip.TripDto;

import ch.qos.logback.classic.Logger;

public class TripScoreProcess implements ItemProcessor<TripDto, TripDto>{

    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    private ScoreService scoreService;
    
    public TripScoreProcess(ScoreService scoreService){
        this.scoreService = scoreService;
    }

    @Override
    @Nullable
    public TripDto process(@NonNull TripDto item) throws Exception {
        Map<String, Object> extScoreMap = scoreService.extractClsScore(item.getTripCts());
        log.info("extScoreMap " + extScoreMap.toString());
        if(extScoreMap != null){
            Double score1 = (Double)extScoreMap.get("힐링");
            Double score2 = (Double)extScoreMap.get("액티비티");
            Double score3 = (Double)extScoreMap.get("계획");
            Double score4 = (Double)extScoreMap.get("즉흥");
            Double score5 = (Double)extScoreMap.get("관광");
            Double score6 = (Double)extScoreMap.get("로컬");
            Double score7 = (Double)extScoreMap.get("미식");
            Double score8 = (Double)extScoreMap.get("쇼핑");
            
            item.setScore1(score1);
            item.setScore2(score2);
            item.setScore3(score3);
            item.setScore4(score4);
            item.setScore5(score5);
            item.setScore6(score6);
            item.setScore7(score7);
            item.setScore8(score8);
        }
        log.info("process item " + item.toString());
        return item;
    }
    
}
