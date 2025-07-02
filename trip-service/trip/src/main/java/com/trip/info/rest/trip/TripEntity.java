package com.trip.info.rest.trip;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document("trip")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripEntity{
    

    @Id
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

    public TripEntity(TripDto tripDto){
        this._Id = tripDto.get_Id();
        this.contentId = tripDto.getContentId();
        this.tripNm = tripDto.getTripNm();
        this.tripCts = tripDto.getTripCts();
        this.mbtia = tripDto.getMbtia();
        this.mbtib = tripDto.getMbtib();
        this.mbtic = tripDto.getMbtic();
        this.mbtid = tripDto.getMbtid();
        this.regUserId = tripDto.getRegUserId();

        this.score1 = tripDto.getScore1();
        this.score2 = tripDto.getScore2();
        this.score3 = tripDto.getScore3();
        this.score4 = tripDto.getScore4();
        this.score5 = tripDto.getScore5();
        this.score6 = tripDto.getScore6();
        this.score7 = tripDto.getScore7();
        this.score8 = tripDto.getScore8();

    }

}
