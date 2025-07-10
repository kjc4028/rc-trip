package com.trip.info.core.smr;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TripSmrRepository extends MongoRepository<TripSmrEntity, String> {
    
    /**
     * 3개의 숫자를 받아서 해당 숫자들을 조합한 totalSum 필드를 내림차순으로 정렬하여 상위 3개 조회
     * 예: (1,2,3) -> totalSum123 내림차순 정렬
     */
    @Query(value = "{}", sort = "{ 'totalSum?0': -1 }")
    List<TripSmrEntity> findTop3ByTotalSumDesc(String searchNum, Pageable pageable);

}
