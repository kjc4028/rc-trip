package com.trip.mbti.rest.trip;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends MongoRepository<TripEntity, String> {
 
    Page<TripEntity> findAll(Pageable pageable);

    Page<TripEntity> findByTripNm(TripEntity tripEntity, Pageable pageable);
    
    Page<TripEntity> findByMbtiaAndMbtibAndMbticAndMbtid(String mbtia, String mbtib, String mbtic, String mbtid, Pageable pageable);


}
