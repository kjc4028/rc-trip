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

    private String tripNm;

    private String tripCts;

    private String mbtia;

    private String mbtib;

    private String mbtic;

    private String mbtid;
    
    private String regUserId;

    public TripEntity(TripDto tripDto){
        this._Id = tripDto.get_Id();
        this.tripNm = tripDto.getTripNm();
        this.tripCts = tripDto.getTripCts();
        this.mbtia = tripDto.getMbtia();
        this.mbtib = tripDto.getMbtib();
        this.mbtic = tripDto.getMbtic();
        this.mbtid = tripDto.getMbtid();
        this.regUserId = tripDto.getRegUserId();
    }

}
