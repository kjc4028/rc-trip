package com.trip.mbti.batch.trip.tripinfo;

import com.trip.mbti.rest.trip.TripEntity;

import lombok.Data;

@Data
public class TripInfoDto {
    private String addr1;
    private String addr2;
    private String booktour;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contentid;
    private String contenttypeid;
    private String createdtime;
    private String eventstartdate;
    private String eventenddate;
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private String mapx;
    private String mapy;
    private String mlevel;
    private String modifiedtime;
    private String areacode;
    private String sigungucode;
    private String tel;
    private String title;
    
    public TripEntity toEntity() {
        return TripEntity.builder()
        .tripNm(title)
        .build();
    }
    
    // public TripEntity toEntity() {
    //     return TripEntity.builder()
    //             .code(code)
    //             .name(name)
    //             .rnum(rnum)
    //             .level(level)
    //             .build();
    // }
    
}
