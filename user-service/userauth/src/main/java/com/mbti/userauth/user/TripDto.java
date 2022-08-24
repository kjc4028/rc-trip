package com.mbti.userauth.user;


import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class TripDto{
    
    @Id
    private String _Id;

    private String tripNm;

    private String tripCts;

    private String mbtia;

    private String mbtib;

    private String mbtic;

    private String mbtid;
    
    private String regUserId;

}
