package com.trip.mbti.client.trip;


import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
        JSONParser jParser = new JSONParser();
        try {
            Object obj = (JSONObject)jParser.parse(responseEntity.getBody());
            
            JSONObject totalObj = (JSONObject)obj;
            System.out.println(totalObj.get("status"));
            System.out.println(totalObj.get("message"));
            System.out.println(totalObj.get("data"));
            JSONArray dataObj = (JSONArray) jParser.parse(totalObj.get("data").toString());
            
            if(dataObj.size()>0){
                for (int i = 0; i < dataObj.size(); i++) {
                    JSONObject jObj = (JSONObject) dataObj.get(i);
                    System.out.println(jObj.get("_Id"));
                    System.out.println(jObj.get("tripNm"));
                    System.out.println(jObj.get("tripCts"));
                    System.out.println("==================");
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseEntity.getBody();
    }

}
