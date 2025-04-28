package com.trip.info.rest.trip;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends MongoRepository<TripEntity, String> {
 
    Page<TripEntity> findAll(Pageable pageable);

    Page<TripEntity> findByTripNm(TripEntity tripEntity, Pageable pageable);

    List<TripEntity> findByRegUserId(TripEntity tripEntity);
    
    Page<TripEntity> findByMbtiaAndMbtibAndMbticAndMbtid(String mbtia, String mbtib, String mbtic, String mbtid, Pageable pageable);

    //Page<TripEntity> findByMbtiaOrMbtia(String mbtia1,String mbtia2, Pageable pageable);
    Page<TripEntity> findByMbtiaInAndMbtibInAndMbticInAndMbtidIn(Set<String> mbtiAList, Set<String> mbtiBList, Set<String> mbtiCList, Set<String> mbtiDList,Pageable pageable);
    
    void deleteByRegUserId(String regUserId);
}
