package com.trip.info.batch.trip.tripinfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.info.batch.common.ApiReaderInterface;
import com.trip.info.rest.trip.TripService;

import ch.qos.logback.classic.Logger;

public class TripInfoReader implements ItemReader<TripInfoDto>, ApiReaderInterface<TripInfoDto>  {
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    String apiServiceKey;
    
    private TripService tripService;
    
    private Iterator<TripInfoDto> iterator;
    
    public TripInfoReader(String apiKey, TripService tripService){
        //this.iterator = inputList.iterator();
        log.info(">>>>>>>>>>batchpoint TripInfoReader param2");
        this.apiServiceKey = apiKey;
        this.tripService = tripService;
        log.info("apiServiceKey " + apiServiceKey);
        List<TripInfoDto> dataList = callApi();
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne dataList" + dataList.toString());
        if(dataList != null){
            this.iterator = dataList.iterator();
        }

    }

    @Override
    @Nullable
    public TripInfoDto read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                log.info(">>>>>>>>>>batchpoint TripInfoReader read");
                log.info(">>>>>>>>>>batchpoint TripInfoReader read iter: "+iterator.toString());
        
                return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public List<TripInfoDto> callApi() {
        
        try {
            List<TripInfoDto> list = new ArrayList<TripInfoDto>();
            
            String encodedApiKey = java.net.URLEncoder.encode(apiServiceKey, "UTF-8");
            String apiUrl = "https://apis.data.go.kr/B551011/KorService1/searchFestival1?MobileOS=ETC&MobileApp=AppTest&eventStartDate=20230101&serviceKey="+encodedApiKey+"&_type=json&numOfRows=100";
            log.info(">>>>>>>>>>batchpoint TripInfoReader callApi apiUrl" + apiUrl);
            // URL 객체 생성
            URL url = new URL(apiUrl);
    
            // URL을 통해 연결을 열고 데이터를 읽을 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            StringBuilder content = new StringBuilder();
    
            // 데이터를 읽어옴
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            
            log.info(">>>contentView " + content.toString());
            list.addAll(recJsonToList(content.toString()));
            reader.close();
            
            return list; 
        } catch (Exception e) {
            log.info(">>>>>>>>>>batchpoint callApi exception" + e);
        }
        return null;
    }

    @Override
    public List<TripInfoDto> recJsonToList(String jsonContent) {
        log.info(">>>>>>>>>>batchpoint TripInfoReader recJsonToList");
        try {
            List<TripInfoDto> list = new ArrayList<TripInfoDto>();
            String callResultJson = jsonContent;
            System.out.println("callResultJson " + callResultJson);
            JSONObject parsed_data = new JSONObject(callResultJson);
            JSONObject responseObj = parsed_data.getJSONObject("response");
            JSONObject bodyObj = responseObj.getJSONObject("body");
            JSONObject itemsObj = bodyObj.getJSONObject("items");
            JSONArray itemObj = itemsObj.getJSONArray("item");
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            list = objectMapper.readValue(itemObj.toString(), new TypeReference<List<TripInfoDto>>() {});
            
    return list;
        } catch (Exception e) {
            log.info("recJsonToList EXCEPTION : " + e);
            return null;
        }
        
    }

    private String callApi(String apiUrl) throws IOException {
        log.info(">>>>>>>>>>batchpoint TripInfoReader callApi apiUrl" + apiUrl);
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return sb.toString();
        } catch (IOException e) {
            log.info(">>>>>>>>>>batchpoint callApi exception" + e);
            throw e;
        }
    }
}
