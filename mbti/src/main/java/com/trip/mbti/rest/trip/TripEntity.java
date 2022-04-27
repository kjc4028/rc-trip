package com.trip.mbti.rest.trip;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("trip")
public class TripEntity {
    
    @Id
    private ObjectId _Id;

    private String tripNm;

    private String tripCts;

}
