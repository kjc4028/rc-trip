package com.trip.info.core.trip;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

    @Query("{ '$or': [ { 'tripCts': { '$exists': false } }, { 'tripCts': { '$eq': null } }, { 'tripCts': { '$size': 0 } } ] }")
    List<TripEntity> findByTripCtsNotExistsOrNullOrEmpty();

    @Query("""
    {
        "$and": [
            { "$or": [ { "score1": null }, { "score1": 0 } ] },
            { "$or": [ { "score2": null }, { "score2": 0 } ] },
            { "$or": [ { "score3": null }, { "score3": 0 } ] },
            { "$or": [ { "score4": null }, { "score4": 0 } ] },
            { "$or": [ { "score5": null }, { "score5": 0 } ] },
            { "$or": [ { "score6": null }, { "score6": 0 } ] },
            { "$or": [ { "score7": null }, { "score7": 0 } ] },
            { "$or": [ { "score8": null }, { "score8": 0 } ] },
        ]
    }
    """)
    List<TripEntity> findByAllScoreNotExistsOrNullOrEmpty();

    List<TripEntity> findByContentIdIn(List<String> contentId);
}
