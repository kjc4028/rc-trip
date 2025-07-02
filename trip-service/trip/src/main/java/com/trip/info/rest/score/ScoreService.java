package com.trip.info.rest.score;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {
    
    
    private static final String API_URL = "https://api-inference.huggingface.co/models/MoritzLaurer/mDeBERTa-v3-base-mnli-xnli";
        private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    public Map<String, Object> extractClsScore(String inputs, String apiKey){
 
        if(inputs == null || "".equals(inputs)){
            return null;
        }

        String convertText = inputs.replaceAll("\\r?\\n", "");
        convertText = convertText.replaceAll("[^a-zA-Z0-9가-힣 .,]", "");
          // 1. 변수 정의
        List<String> candidateLabels = List.of("힐링","액티비티","계획","즉흥","관광","로컬","미식","쇼핑");

        // List를 JSON 배열 형태의 문자열로 변환
        String labelsJsonArray = candidateLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(", "));

        // 2. 템플릿에 변수 삽입
        String jsonInputString = """
                {
                  "inputs": "%s",
                  "parameters": {
                    "candidate_labels": [%s]
                  }
                }
                """.formatted(convertText, labelsJsonArray);
        log.info("jsonInputString " + jsonInputString);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Status Code: " + response.statusCode());
            log.info("Response Body: " + response.body());

            if(response.statusCode() == 200){
                        // Gson 객체 생성
                Gson gson = new Gson();

                // JSON 문자열을 Map<String, Object> 형태로 변환
                // labels는 String 배열, scores는 double 배열로 인식됩니다.
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> dataMap = gson.fromJson(response.body(), type);

                // 결과 확인
                log.info("파싱된 데이터: " + dataMap);

                // "labels"와 "scores"를 각각의 리스트로 가져오기
                java.util.List<String> labels = (java.util.List<String>) dataMap.get("labels");
                java.util.List<Double> scores = (java.util.List<Double>) dataMap.get("scores");

                // 최종적으로 원하는 label:score 형태의 Map으로 변환
                java.util.Map<String, Object> resultMap = new java.util.LinkedHashMap<>(); // 순서 보장을 위해 LinkedHashMap 사용
                for (int i = 0; i < labels.size(); i++) {
                    resultMap.put(labels.get(i), scores.get(i));
                }
                
                log.info("resultMap " + resultMap.toString());
                return resultMap;
            }
        } catch (IOException e) {
            log.error("extractClsScore ex "+ e);
            return null;
        } catch (Exception e) {
            log.error("extractClsScore ex "+ e);
            return null;
        }
        return null;

    }
}
