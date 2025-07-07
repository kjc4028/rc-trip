package com.trip.info.rest.trip;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.addFields;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.out;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<TripEntity> findOneById(String id) {
        return tripRepository.findById(id); 
     }

    public List<TripEntity> findAllTripEntity() {
       return tripRepository.findAll(); 
    }

    public Page<TripEntity> findAllTripPage(int pageNum, int perPage){
        return tripRepository.findAll(PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public Page<TripEntity> findSearchTripNmPage(TripEntity tripEntity, int pageNum, int perPage){
        return tripRepository.findByTripNm(tripEntity, PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public Page<TripEntity> findSearchTripMbtiPage(TripEntity tripEntity, int pageNum, int perPage){
        return tripRepository.findByMbtiaAndMbtibAndMbticAndMbtid(tripEntity.getMbtia(), tripEntity.getMbtib(),tripEntity.getMbtic(),tripEntity.getMbtid(),PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }
    
    public Page<TripEntity> findSearchTripMbtiMultiPage(Set<String> mbtiaList, Set<String> mbtibList, Set<String> mbticList, Set<String> mbtidList, int pageNum, int perPage){
        return tripRepository.findByMbtiaInAndMbtibInAndMbticInAndMbtidIn(mbtiaList, mbtibList ,mbticList, mbtidList, PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public List<TripEntity> findByRegUserId(TripEntity tripEntiity){
        return tripRepository.findByRegUserId(tripEntiity);
    }

    public void save(TripEntity tripEntity){
        tripRepository.save(tripEntity);
    }
    
    public void delete(TripEntity tripEntity){
        tripRepository.delete(tripEntity);
    }

    public void deleteByRegUserId(String regUserId){
        tripRepository.deleteByRegUserId(regUserId);
    }

    public List<TripEntity> findByTripCtsNotExistsOrNullOrEmpty(){
        return tripRepository.findByTripCtsNotExistsOrNullOrEmpty();
    }
    
    public List<TripEntity> findByAllScoreNotExistsOrNullOrEmpty(){
        return tripRepository.findByAllScoreNotExistsOrNullOrEmpty();
    }

        /**
     * 조합을 생성하는 메서드
     * @param arr 원본 숫자 배열
     * @param k 선택할 개수
     * @return 생성된 모든 조합의 리스트
     */
    public static List<List<Integer>> createCombinations(int[] arr, int k) {
        List<List<Integer>> result = new ArrayList<>();
        // 재귀 함수 호출
        findCombinations(arr, result, new ArrayList<>(), k, 0);
        return result;
    }

    /**
     * 재귀적으로 조합을 찾는 헬퍼 함수
     * @param arr 원본 배열
     * @param result 최종 결과를 담을 리스트
     * @param currentCombination 현재 만들어지고 있는 조합 리스트
     * @param k 선택할 개수
     * @param start 탐색을 시작할 인덱스
     */
    private static void findCombinations(int[] arr, List<List<Integer>> result, List<Integer> currentCombination, int k, int start) {
        // 1. 현재 조합이 k개가 되면 최종 결과에 추가하고 종료 (재귀의 탈출 조건)
        if (currentCombination.size() == k) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        // 2. start 인덱스부터 배열 끝까지 순회
        for (int i = start; i < arr.length; i++) {
            // 현재 숫자를 조합에 추가
            currentCombination.add(arr[i]);
            // 다음 숫자를 찾기 위해 재귀 호출 (다음 탐색 시작 인덱스는 i + 1)
            findCombinations(arr, result, currentCombination, k, i + 1);
            // 재귀 호출이 끝나면, 다음 탐색을 위해 마지막에 추가했던 숫자를 제거 (백트래킹)
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
    
    /**
     * 단일그룹 집계
     */
    public void runAggregationWithProjection() {

        Aggregation aggregation = newAggregation(
            addFields().addField("totalSum123").withValue(
                ArithmeticOperators.Add.valueOf("score1").add("score2").add("score3")
            ).build(),
            addFields().addField("totalSum456").withValue(
                ArithmeticOperators.Add.valueOf("score4").add("score5").add("score6")
            ).build(),
            // sort(Sort.Direction.DESC, "totalSum123"),
            // limit(100),
            project("contentId", "totalSum123", "totalSum456").andExclude("_id"),
        out("trip_smr")
    );
    mongoTemplate.aggregate(aggregation, "trip", Void.class);
    }

        /**
     * 동적으로 정의된 그룹에 따라 필드들을 합산하고, 결과를 새 컬렉션에 저장합니다.
     * @param scoreGroups        집계할 그룹 정보 (Key: 생성할 필드명, Value: 합산할 필드 리스트)
     * @param sourceCollection   원본 컬렉션 이름
     * @param destinationCollection 결과를 저장할 컬렉션 이름
     */
    public void createAggregatedCollection(Map<String, List<String>> scoreGroups,
                                           String sourceCollection,
                                           String destinationCollection) {

        // 1. 파이프라인의 각 단계를 담을 리스트를 생성합니다.
        List<AggregationOperation> pipeline = new ArrayList<>();

        // 2. 반복문을 사용하여 동적으로 $addFields 단계를 생성하고 파이프라인에 추가합니다.
        for (Map.Entry<String, List<String>> entry : scoreGroups.entrySet()) {
            String newFieldName = entry.getKey(); // 예: "totalSum123"
            List<String> fieldsToSum = entry.getValue(); // 예: ["score1", "score2", "score3"]

            // $add 연산자를 동적으로 생성합니다.
            ArithmeticOperators.Add addOperator = ArithmeticOperators.Add.valueOf(fieldsToSum.get(0));
            for (int i = 1; i < fieldsToSum.size(); i++) {
                addOperator = addOperator.add(fieldsToSum.get(i));
            }

            // $addFields 단계를 생성하여 파이프라인에 추가합니다.
            AddFieldsOperation addFieldsStage = Aggregation.addFields()
                .addField(newFieldName)
                .withValue(addOperator)
                .build();
            pipeline.add(addFieldsStage);
        }

        // 3. 동적으로 $project 단계를 생성합니다.
        List<String> projectionFields = new ArrayList<>();
        projectionFields.add("contentId"); // 기본적으로 포함할 필드
        projectionFields.addAll(scoreGroups.keySet()); // 동적으로 생성된 합계 필드들을 추가

        pipeline.add(
            project(projectionFields.toArray(new String[0])).andExclude("_id")
        );

        // 4. 파이프라인의 마지막에 $out 단계를 추가합니다.
        pipeline.add(out(destinationCollection));

        // 5. 생성된 모든 단계를 조합하여 최종 Aggregation 객체를 만듭니다.
        Aggregation aggregation = newAggregation(pipeline);

        // 6. 집계 쿼리를 실행합니다.
        mongoTemplate.aggregate(aggregation, sourceCollection, Void.class);
    }

    /**
     * score 분류에 따른 모든 조합 집계
     */
    public void aggreatedTotalCase() {
        // 합산할 그룹 정의 (요청에 따라 합산 필드명 변경)
        // Map<String, List<String>> groups = Map.of(
        //     "totalSum123", List.of("score1", "score2", "score3"),
        //     "totalSum456", List.of("score4", "score5", "score6"),
        //     "totalSum136", List.of("score1", "score3", "score6")
        // );
        Map<String, List<String>> groups = new HashMap<String, List<String>>();

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8};
        int k = 3;
        List<List<Integer>> combinations = createCombinations(numbers, k);
        for(List<Integer> combination : combinations){
            System.out.println(combination);
            String totalSum = "totalSum"+combination.get(0)+combination.get(1)+combination.get(2);
            groups.put(totalSum, List.of("score"+combination.get(0), "score"+combination.get(1), "score"+combination.get(2)));
        }


        createAggregatedCollection(groups, "trip", "trip_smr");
        System.out.println("'smr' 컬렉션 생성이 요청되었습니다.");
    }

}
