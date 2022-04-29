package com.trip.mbti.client.trip;


import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/client")
@RestController
public class TripClientController {
    
    @GetMapping("/trip")
    @ResponseBody
    public String tripList(){
        RestTemplate restTemp = new RestTemplate();
        String url = "http://localhost:8080/rest/trip";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = restTemp.getForEntity(url,String.class, requestEntity);
        
        return responseEntity.getBody();
    }

}
