package com.trip.info.batch.trip.category;

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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.info.batch.common.ApiReaderInterface;
import com.trip.info.batch.exception.DuplicationDataException;
import com.trip.info.core.category.CategoryEntity;
import com.trip.info.core.category.CategoryService;

import ch.qos.logback.classic.Logger;

@Component
public class CategoryItemReaderByLevel implements ItemReader<CategoryDto>, ApiReaderInterface<CategoryDto>  {
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    String apiServiceKey;

    String categoryLevel;

    private CategoryService categoryService;

    private Iterator<CategoryDto> iterator;

    public CategoryItemReaderByLevel(){

    }

    public CategoryItemReaderByLevel(List<CategoryDto> inputList){
        this.iterator = inputList.iterator();
    }

    public CategoryItemReaderByLevel(String apiKey, String categoryLevel, CategoryService categoryService){
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne param3");
        this.apiServiceKey = apiKey;
        this.categoryLevel = categoryLevel;
        this.categoryService = categoryService;
        log.info("apiServiceKey " + apiServiceKey);
        log.info("categoryLevel " + categoryLevel);
        List<CategoryDto> dataList = callApi();
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne dataList" + dataList.toString());
        if(dataList != null){
            this.iterator = dataList.iterator();
        }
    }

    /**
     * 카테고리 코드 데이터 공공데이터API 호출
     */
    @Override
    public List<CategoryDto> callApi(){
        log.info(">>>>>>>>>>batchpoint callAPi");
        try {
        List<CategoryDto> list = new ArrayList<CategoryDto>();

            StringBuffer sb = new StringBuffer();

            if(categoryLevel != null){
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setLevel(categoryLevel);
                if("1".equals(categoryLevel)){
                    List<CategoryEntity> existLv1Category = categoryService.findByLevel("1");
                    
                    if(!existLv1Category.isEmpty()){
                        throw new DuplicationDataException("데이터 존재");
                    }

                    String encodedApiKey = java.net.URLEncoder.encode(apiServiceKey, "UTF-8");
                    String apiUrl = "https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+encodedApiKey+"&_type=json";
                    log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne apiUrl" + apiUrl);
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
                    List<CategoryEntity> existLv2Category = categoryService.findByLevel("2");
                    
                    if(!existLv2Category.isEmpty()){
                        throw new DuplicationDataException("데이터 존재");
                    }
                    categoryEntity.setLevel("1");
                    List<CategoryEntity> codeList = categoryService.findByLevel("1");
                    log.info("category find by level codeList" + codeList.toString());
                    for (CategoryEntity category : codeList) {
                        sb.append("&cat1="+category.getCode());

                        String encodedApiKey = java.net.URLEncoder.encode(apiServiceKey, "UTF-8");
                        String apiUrl = "https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+encodedApiKey+"&_type=json"+sb.toString();

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
                    List<CategoryEntity> existLv3Category = categoryService.findByLevel("3");
                    
                    if(!existLv3Category.isEmpty()){
                        throw new DuplicationDataException("데이터 존재");
                    }
                    List<CategoryEntity> codeList1 = categoryService.findByLevel("1");
                    
                
                    for (CategoryEntity category : codeList1) {
                        List<CategoryEntity> codeList2 = categoryService.findByCodeStartsWithAndLevel(category.getCode(),"2");
                        for (CategoryEntity category2 : codeList2) {
                            sb.append("&cat1="+category.getCode()+"&cat2="+category2.getCode());

                            String encodedApiKey = java.net.URLEncoder.encode(apiServiceKey, "UTF-8");
                            String apiUrl = "https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+encodedApiKey+"&_type=json"+sb.toString();

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
            
            return list;
        } catch (Exception e) {
            log.info(">>>>>>>>>>batchpoint callApi exception" + e);
        }
        return null;
    }

    @Override
    @Nullable
    public CategoryDto read() throws Exception {
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne read");
        if (iterator != null && iterator.hasNext()) {
            CategoryDto item = iterator.next();
            log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne read item: " + item);
            return item;
        }
        return null;
    }

    /*
     * 수신된 json 데이터 list형으로 반환
     */
    @Override
    public List<CategoryDto> recJsonToList(String jsonContent){
        log.info(">>>>>>>>>>batchpoint CategoryItemReaderLvOne recJsonToList");
        try {
            List<CategoryDto> list = new ArrayList<CategoryDto>();
            String callResultJson = jsonContent;
            log.info("callResultJson " + callResultJson);
            JSONObject parsed_data = new JSONObject(callResultJson);
            JSONObject responseObj = parsed_data.getJSONObject("response");
            JSONObject bodyObj = responseObj.getJSONObject("body");
            JSONObject itemsObj = bodyObj.getJSONObject("items");
            JSONArray itemObj = itemsObj.getJSONArray("item");
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            list = objectMapper.readValue(itemObj.toString(), new TypeReference<List<CategoryDto>>() {});
            
    return list;
        } catch (Exception e) {
            log.info("recJsonToList EXCEPTION : " + e);
            return null;
        }

    }

    
}
