package com.trip.info.batch.trip.tripDtlInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.info.core.trip.TripDto;
import com.trip.info.core.trip.TripService;

public class TripDtlInfoProcess implements ItemProcessor<TripDto,TripDto> {

    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    String apiServiceKey;
    
    private TripService tripService;

    
    public TripDtlInfoProcess(String apiKey, TripService tripService){
        log.info(">>>>>>>>>>batchpoint TripDtlInfoProcess param2");
        this.apiServiceKey = apiKey;
        this.tripService = tripService;
        log.info("apiServiceKey " + apiServiceKey);
    }


 
    
    public TripDtlInfoDto callApi(String contentId) {
        
        try {
            TripDtlInfoDto dtlInfoDto = new TripDtlInfoDto();
            
            String encodedApiKey = java.net.URLEncoder.encode(apiServiceKey, "UTF-8");
            String apiUrl = "https://apis.data.go.kr/B551011/KorService2/detailCommon2?serviceKey="+encodedApiKey+"&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId="+contentId+"&numOfRows=10&pageNo=1";
            log.info(">>>>>>>>>>batchpoint TripDtlInfoReader callApi apiUrl" + apiUrl);
            // URL 객체 생성
            URL url = new URL(apiUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(1000); //서버 연결 제한 시간
            httpConn.setReadTimeout(1000);  // 서버 연결 후 데이터 read 제한 시간

            // URL을 통해 연결을 열고 데이터를 읽을 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String line;
            StringBuilder content = new StringBuilder();
    
            // 데이터를 읽어옴
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            
            log.info(">>>contentView " + content.toString());
            dtlInfoDto = recJsonToDto(content.toString());
            reader.close();
            
            return dtlInfoDto; 
        } catch (Exception e) {
            log.info(">>>>>>>>>>batchpoint callApi exception" + e);
            return null;
        }
    }


    public TripDtlInfoDto recJsonToDto(String jsonContent) {
        log.info(">>>>>>>>>>batchpoint TripDtlInfoReader recJsonToList");
        try {
            TripDtlInfoDto dtlInfoDto = new TripDtlInfoDto();
            String callResultJson = jsonContent;
            log.info("callResultJson " + callResultJson);
            JSONObject parsed_data = new JSONObject(callResultJson);
            JSONObject responseObj = parsed_data.getJSONObject("response");
            JSONObject bodyObj = responseObj.getJSONObject("body");
            JSONObject itemsObj = bodyObj.getJSONObject("items");
            JSONArray itemObj = itemsObj.getJSONArray("item");
            log.info(">>>dtlinfotostr0 "+itemObj.toString());
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // dtlInfoDto = objectMapper.readValue(itemObj.get(0).toString(), TripDtlInfoDto.class);
            dtlInfoDto = objectMapper.readValue(itemObj.get(0).toString(), TripDtlInfoDto.class);
            
            log.info(">>>dtlinfotostr "+dtlInfoDto.toString());
    return dtlInfoDto;
        } catch (Exception e) {
            log.info("recJsonToList EXCEPTION : " + e);
            return null;
        }
        
    }


    @Override
    public TripDto process(TripDto item) throws Exception {
        String contentId = item.getContentId();
        TripDtlInfoDto dtlInfoDto = callApi(contentId);
        if(dtlInfoDto != null){
            item.setTripCts(dtlInfoDto.getOverview());
        }

        return item;
    }
    
}
