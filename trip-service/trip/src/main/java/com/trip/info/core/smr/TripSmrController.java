package com.trip.info.core.smr;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.info.core.common.Message;
import com.trip.info.core.trip.TripEntity;
import com.trip.info.core.trip.TripService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trips/trip-smr")
@RequiredArgsConstructor
public class TripSmrController {
    
    private final TripSmrService tripSmrService;

    private final TripService tripService;
    
    /**
     * 3개의 숫자를 받아서 해당 숫자들을 조합한 totalSum 필드를 내림차순으로 정렬하여 상위 3개 조회
     * 예: GET /api/trip-smr/top3?num1=1&num2=2&num3=3
     */
    @GetMapping("/top3")
    @ResponseBody
    public ResponseEntity<Message> getTop3ByTotalSumDesc(
            @RequestParam("selectedItems") List<String> selectedPreference) {
                Message message = new Message();
                ObjectMapper om = new ObjectMapper();
                HttpHeaders headers = new HttpHeaders();
            try {

                // 1. 오름차순 정렬
                selectedPreference.sort(String::compareTo);

                // 2. 하나의 문자열로 합치기
                String combinedPreference = String.join("", selectedPreference);

                List<TripEntity> tripList = tripService.findTop3ByPreference(combinedPreference);

                headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
                HashMap<String, Object> responseData = new HashMap<String, Object>();
                responseData.put("totalCnt", tripList.size());
                String resData = om.writeValueAsString(tripList) + om.writeValueAsString(responseData);
                message.setStatus(HttpStatus.OK);
                message.setData(tripList);
                message.setMessage("정상호출");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            

            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
   
  
} 