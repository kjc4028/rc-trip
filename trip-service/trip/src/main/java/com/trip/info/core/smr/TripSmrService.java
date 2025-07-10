package com.trip.info.core.smr;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripSmrService {
    
    private final TripSmrRepository tripSmrRepository;
    private final MongoTemplate mongoTemplate;
    
    /**
     * 3개의 숫자를 받아서 해당 숫자들을 조합한 totalSum 필드를 내림차순으로 정렬하여 상위 3개 조회
     * 예: (1,2,3) -> totalSum123 내림차순 정렬
     * 해당 경우는 상위3종 선 조회 후 정렬을 진행하여 기능에 맞지 않음
     */
    public List<TripSmrEntity> findTop3ByTotalSumDesc(String combinedPreference) { 
        return tripSmrRepository.findTop3ByTotalSumDesc(combinedPreference, PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "TotalSum"+combinedPreference)));
    }
    
     
    /**
     * MongoTemplate을 사용하여 동적으로 필드명을 생성하고 쿼리하는 메서드
     * 이 방법이 더 유연하고 안전합니다.
     */
    public List<TripSmrEntity> findTop3ByDynamicTotalSumDesc(String combinedPreference) {
        // 필드명 동적 생성
        String fieldName = "totalSum" + combinedPreference;
        
        // 쿼리 생성
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, fieldName));
        query.limit(3); // 상위 3개만 조회
        
        return mongoTemplate.find(query, TripSmrEntity.class);
    }
    
    /**
     * contentId 조건과 함께 동적으로 필드명을 생성하여 쿼리하는 메서드
     */
    public List<TripSmrEntity> findTop3ByContentIdAndDynamicTotalSumDesc(String contentId, int num1, int num2, int num3) {
        // 필드명 동적 생성
        String fieldName = "totalSum" + num1 + num2 + num3;
        
        // 쿼리 생성
        Query query = new Query(Criteria.where("contentId").is(contentId));
        query.with(Sort.by(Sort.Direction.DESC, fieldName));
        query.limit(3); // 상위 3개만 조회
        
        return mongoTemplate.find(query, TripSmrEntity.class);
    }

} 
