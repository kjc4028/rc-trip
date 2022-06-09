package com.trip.mbti.client.trip;


import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/client")
public class TripClientController {
    
    @GetMapping("/trip")
    public String tripList(Model model){
        RestTemplate restTemp = new RestTemplate();
        String url = "http://localhost:8080/rest/trips";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = restTemp.getForEntity(url,String.class, requestEntity);
        JSONParser jParser = new JSONParser();
        try {
            Object obj = (JSONObject)jParser.parse(responseEntity.getBody());
            
            JSONObject totalObj = (JSONObject)obj;
            System.out.println(totalObj.get("status"));
            System.out.println(totalObj.get("message"));
            System.out.println(totalObj.get("data"));
            JSONArray dataObj = (JSONArray) jParser.parse(totalObj.get("data").toString());
            
            //TripClientDto tripDto = new TripClientDto();
            List<TripClientDto> tripList = new ArrayList<>();
            if(dataObj.size()>0){
                for (int i = 0; i < dataObj.size(); i++) {
                    TripClientDto tripDto = new TripClientDto();
                    JSONObject jObj = (JSONObject) dataObj.get(i);
                    System.out.println(jObj.get("_Id"));
                    System.out.println(jObj.get("tripNm"));
                    System.out.println(jObj.get("tripCts"));
                    System.out.println("==================");
                    tripDto.setTripNm(jObj.get("tripNm").toString());
                    tripDto.setTripCts(jObj.get("tripCts").toString());
                    tripDto.setTripId(jObj.get("_Id").toString());
                    tripList.add(tripDto);
                }
            }
            model.addAttribute("tripList", tripList);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //return responseEntity.getBody();
        return "client/trip/tripList";
    }

}
