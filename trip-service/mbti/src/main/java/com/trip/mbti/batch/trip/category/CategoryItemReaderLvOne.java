package com.trip.mbti.batch.trip.category;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoryItemReaderLvOne implements ItemReader<CategoryVo>  {
    
    String apiServiceKey;


    private Iterator<CategoryVo> iterator;

        public CategoryItemReaderLvOne(List<CategoryVo> inputList){
        this.iterator = inputList.iterator();


    }

    public CategoryItemReaderLvOne(String apiKey){
        //this.iterator = inputList.iterator();
        System.out.println("apiServiceKey " + apiServiceKey);
        this.apiServiceKey = apiKey;
        List<CategoryVo> dataList = callAPi();
        if(dataList != null){
            this.iterator = dataList.iterator();
        }

    }

public List<CategoryVo> callAPi(){
    try {
        // URL 객체 생성
        URL url = new URL("https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+apiServiceKey+"&_type=json");

        // URL을 통해 연결을 열고 데이터를 읽을 BufferedReader 생성
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));



        String line;
        StringBuilder content = new StringBuilder();

        // 데이터를 읽어옴
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        String callResultJson = content.toString();
        System.out.println("callResultJson " + callResultJson);
        JSONObject parsed_data = new JSONObject(callResultJson);
        JSONObject responseObj = parsed_data.getJSONObject("response");
        JSONObject bodyObj = responseObj.getJSONObject("body");
        JSONObject itemsObj = bodyObj.getJSONObject("items");
        JSONArray itemObj = itemsObj.getJSONArray("item");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        List<CategoryVo> list = objectMapper.readValue(itemObj.toString(), new TypeReference<List<CategoryVo>>() {});

        for (CategoryVo categoryVo : list) {
            System.out.println("Name: " + categoryVo.getCode());			
        }

        // 읽어온 데이터 출력
        System.out.println(content.toString());



        reader.close();
        return list;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    @Override
    @Nullable
    public CategoryVo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        System.out.println("CategoryItemReaderLvOne readtest>>> ");
		


        return iterator.hasNext() ? iterator.next() : null;
    }


    
}
