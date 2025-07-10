package com.trip.info.core.common;

import com.trip.info.core.trip.TripEntity;

import lombok.Data;

@Data
public class SearchDto {
    
    private String srchKwd;

    private String srchCts;

    private String srchMbtia;
    
    private String srchMbtib;
    
    private String srchMbtic;
    
    private String srchMbtid;
    
    private String srchCls;

    public TripEntity toEntity() {
        return TripEntity.builder()
                .tripNm(srchKwd)
                .mbtia(srchMbtia)
                .mbtib(srchMbtib)
                .mbtic(srchMbtic)
                .mbtid(srchMbtid)
                .build();
    }
}
