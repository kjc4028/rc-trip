package com.trip.mbti.rest.trip;


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

    private String tripNm;

    private String tripCts;

    private String mbtia;

    private String mbtib;

    private String mbtic;

    private String mbtid;
    
    private String regUserId;



}
