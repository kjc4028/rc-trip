package com.trip.info.core.smr;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document("trip_smr")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripSmrEntity {
    
    @Id
    private String _Id;

    private String contentId;

    Double totalSum123;
    Double totalSum124;
    Double totalSum125;
    Double totalSum126;
    Double totalSum127;
    Double totalSum128;
    Double totalSum134;
    Double totalSum135;
    Double totalSum136;
    Double totalSum137;
    Double totalSum138;
    Double totalSum145;
    Double totalSum146;
    Double totalSum147;
    Double totalSum148;
    Double totalSum156;
    Double totalSum157;
    Double totalSum158;
    Double totalSum167;
    Double totalSum168;
    Double totalSum178;
    Double totalSum234;
    Double totalSum235;
    Double totalSum236;
    Double totalSum237;
    Double totalSum238;
    Double totalSum245;
    Double totalSum246;
    Double totalSum247;
    Double totalSum248;
    Double totalSum256;
    Double totalSum257;
    Double totalSum258;
    Double totalSum267;
    Double totalSum268;
    Double totalSum278;
    Double totalSum345;
    Double totalSum346;
    Double totalSum347;
    Double totalSum348;
    Double totalSum356;
    Double totalSum357;
    Double totalSum358;
    Double totalSum367;
    Double totalSum368;
    Double totalSum378;
    Double totalSum456;
    Double totalSum457;
    Double totalSum458;
    Double totalSum467;
    Double totalSum468;
    Double totalSum478;
    Double totalSum567;
    Double totalSum568;
    Double totalSum578;
    Double totalSum678;

}
