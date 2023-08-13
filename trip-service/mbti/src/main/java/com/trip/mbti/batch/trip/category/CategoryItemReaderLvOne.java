package com.trip.mbti.batch.trip.category;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mbti.rest.category.CategoryEntity;
import com.trip.mbti.rest.category.CategoryService;

import ch.qos.logback.classic.Logger;

@Component
public class CategoryItemReaderLvOne implements ItemReader<CategoryDto>  {
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    String apiServiceKey;

    String categoryLevel;

    private CategoryService categoryService;

    private Iterator<CategoryDto> iterator;

    public CategoryItemReaderLvOne(){

    }

    public CategoryItemReaderLvOne(List<CategoryDto> inputList){
        this.iterator = inputList.iterator();


    }

    public CategoryItemReaderLvOne(String apiKey, String categoryLevel, CategoryService categoryService){
        //this.iterator = inputList.iterator();
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne param3");
        this.apiServiceKey = apiKey;
        this.categoryLevel = categoryLevel;
        this.categoryService = categoryService;
        System.out.println("apiServiceKey " + apiServiceKey);
        System.out.println("categoryLevel " + categoryLevel);
        List<CategoryDto> dataList = callAPi();
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne dataList" + dataList.toString());
        if(dataList != null){
            this.iterator = dataList.iterator();
        }

    }

public List<CategoryDto> callAPi(){
    log.info(">>>>>>>>>>batchpoint callAPi");
    try {
        
        List<CategoryDto> list = new ArrayList<CategoryDto>();

        StringBuffer sb = new StringBuffer();

        if(categoryLevel != null){
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setLevel(categoryLevel);
            if("1".equals(categoryLevel)){
                String apiUrl = "https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+apiServiceKey+"&_type=json";


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
        
                list.addAll(recJsonToList(content.toString()));
                reader.close();
            }
            if("2".equals(categoryLevel)){
                categoryEntity.setLevel("1");
                List<CategoryEntity> codeList = categoryService.findByLevel("1");
                System.out.println("category find by level codeList" + codeList.toString());
                for (CategoryEntity category : codeList) {
                    sb.append("&cat1="+category.getCode());

                    String apiUrl = "https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+apiServiceKey+"&_type=json"+sb.toString();


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
            
                    list.addAll(recJsonToList(content.toString()));
                    reader.close();
                    sb = new StringBuffer();
                }
                                
            
            }
            if("3".equals(categoryLevel)){
                List<CategoryEntity> codeList1 = categoryService.findByLevel("1");
                
               
                for (CategoryEntity category : codeList1) {
                    List<CategoryEntity> codeList2 = categoryService.findByCodeStartsWithAndLevel(category.getCode(),"2");
                    for (CategoryEntity category2 : codeList2) {
                        sb.append("&cat1="+category.getCode()+"&cat2="+category2.getCode());

                    String apiUrl = "https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+apiServiceKey+"&_type=json"+sb.toString();


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
            
                    list.addAll(recJsonToList(content.toString()));
                    reader.close();
                    sb = new StringBuffer();
                    }
                }            
            }
        }


        
        for (CategoryDto categoryVo : list) {
            log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne categoryVo.getCode() : " + categoryVo.getCode());		
        }

        // 읽어온 데이터 출력
        //System.out.println(content.toString());



        
        return list;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    @Override
    @Nullable
    public CategoryDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne read");
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne read iter: "+iterator.toString());

        return iterator.hasNext() ? iterator.next() : null;
    }

    public List<CategoryDto> recJsonToList(String jsonContent){
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne recJsonToList");
        try {
            List<CategoryDto> list = new ArrayList<CategoryDto>();
            String callResultJson = jsonContent;
            System.out.println("callResultJson " + callResultJson);
            JSONObject parsed_data = new JSONObject(callResultJson);
            JSONObject responseObj = parsed_data.getJSONObject("response");
            JSONObject bodyObj = responseObj.getJSONObject("body");
            JSONObject itemsObj = bodyObj.getJSONObject("items");
            JSONArray itemObj = itemsObj.getJSONArray("item");
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            list = objectMapper.readValue(itemObj.toString(), new TypeReference<List<CategoryDto>>() {});
            
            // list.addAll(objectMapper.readValue(itemObj.toString(), new TypeReference<List<CategoryDto>>() {}));
    
            return list;
        } catch (Exception e) {
            log.info("recJsonToList EXCEPTION : " + e);
            return null;
        }

    }

    
}
