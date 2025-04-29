package com.trip.mbti.batch.common;

import java.util.List;

public interface ApiReaderInterface<E> {
    
    /**
     * api 호출하여 데이터 목록 수신
     * @return
     */
    List<E> callApi();
        
    
    /**
     * 수신 json 데이터 문자열 -> 리스트형으로 반환
     * @param jsonContent
     * @return
     */
    List<E> recJsonToList(String jsonContent);
    
}
