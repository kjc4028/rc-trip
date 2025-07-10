package com.trip.info.core.trip;

import lombok.Data;

@Data
public class TripRequestDto {
    
    private String _Id;

    private String tripNm;

    private String tripCts;

    private String mbtia;

    private String mbtib;

    private String mbtic;

    private String mbtid;
    
    private String regUserId;

    public TripEntity toEntity() {
        return TripEntity.builder()
                ._Id(_Id)
                .tripNm(tripNm)
                .tripCts(tripCts)
                .mbtia(mbtia)
                .mbtib(mbtib)
                .mbtic(mbtic)
                .mbtid(mbtid)
                .regUserId(regUserId)
                .build();
    }

}
