package com.trip.info.rest.trip;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDto{

    private String _Id;
    
    private String contentId;

    private String tripNm;

    private String tripCts;

    private String mbtia;

    private String mbtib;

    private String mbtic;

    private String mbtid;
    
    private String regUserId;

    private Double score1;
    private Double score2;
    private Double score3;
    private Double score4;
    private Double score5;
    private Double score6;
    private Double score7;
    private Double score8;


    public static TripDto from(TripEntity tripEntity) {
        TripDto dto = new TripDto();
        dto.set_Id(tripEntity.get_Id());
        dto.setContentId(tripEntity.getContentId());
        dto.setTripNm(tripEntity.getTripNm());
        dto.setTripCts(tripEntity.getTripCts());
        dto.setMbtia(tripEntity.getMbtia());
        dto.setMbtib(tripEntity.getMbtib());
        dto.setMbtic(tripEntity.getMbtic());
        dto.setMbtid(tripEntity.getMbtid());
        dto.setRegUserId(tripEntity.getRegUserId());
        dto.setScore1(tripEntity.getScore1());
        dto.setScore2(tripEntity.getScore2());
        dto.setScore3(tripEntity.getScore3());
        dto.setScore4(tripEntity.getScore4());
        dto.setScore5(tripEntity.getScore5());
        dto.setScore6(tripEntity.getScore6());
        dto.setScore7(tripEntity.getScore7());
        dto.setScore8(tripEntity.getScore8());
       
        return dto;
    }


       public TripEntity toEntity() {
        return TripEntity.builder()
        .tripNm(tripNm)
        .tripCts(tripCts)
        .contentId(contentId)
        ._Id(_Id)
        .regUserId(regUserId)
        .score1(score1)
        .score2(score2)
        .score3(score3)
        .score4(score4)
        .score5(score5)
        .score6(score6)
        .score7(score7)
        .score8(score8)
        .build();
    }


}
