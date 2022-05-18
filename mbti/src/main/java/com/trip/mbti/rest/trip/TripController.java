package com.trip.mbti.rest.trip;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trip.mbti.rest.common.Message;
import com.trip.mbti.rest.common.PageDto;
import com.trip.mbti.rest.common.SearchDto;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {
 
    @Autowired
    private TripService tripService;

    @GetMapping("/rest/trip/test")
    @ResponseBody
    public ResponseEntity tirpAllList(TripEntity tripentity){
        try {
            List<TripEntity> list =  tripService.findAllTripEntity();
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            HashMap<String, Object> responseData = new HashMap<String, Object>();
            responseData.put("totalCnt", list.size());
            String resData = om.writeValueAsString(list) + om.writeValueAsString(responseData) ;
            Message message = new Message();
            message.setStatus(HttpStatus.OK);
            message.setData(resData);
            message.setMessage("정상호출");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            Message message = new Message();
            message.setStatus(HttpStatus.BAD_GATEWAY);
            message.setMessage("호출중 오류 발생");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }
       
    }

    /**
     * trip 페이징 조회
     * @param request
     * @param tripentity
     * @return
     */
    @GetMapping(path="/rest/trip")
    @ResponseBody
    public ResponseEntity tirpAllPage(HttpServletRequest request, TripEntity tripentity){
        try {
            Page<TripEntity> page =  tripService.findAllTripPage(1, 10);
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            Message message = new Message();
            String resData = "";
            HashMap<String, Object> resMap = new HashMap<>();

            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            resMap.put("data", page.getContent());
            resMap.put("totalCnt", page.getTotalElements());
            resMap.put("totalPages", page.getTotalPages());
            resMap.put("currentPage", page.getNumber());

            resData = om.writeValueAsString(resMap);

            message.setStatus(HttpStatus.OK);
            message.setData(resData);
            message.setMessage("정상호출");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            Message message = new Message();
            message.setStatus(HttpStatus.BAD_GATEWAY);
            message.setMessage("호출중 오류 발생");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }
       
    }

    @GetMapping(path="/rest/trip-search-base")
    @ResponseBody
    public ResponseEntity tirpSearchPage(HttpServletRequest request, SearchDto searchDto, PageDto pageDto) throws ParseException{
        try {
            
            TripEntity srchTripEntity = new TripEntity();
            srchTripEntity.setMbtia(searchDto.getSrchMbtia());
            srchTripEntity.setMbtib(searchDto.getSrchMbtib());
            srchTripEntity.setMbtic(searchDto.getSrchMbtic());
            srchTripEntity.setMbtid(searchDto.getSrchMbtid());

            srchTripEntity.setTripNm(searchDto.getSrchKwd());
            Page<TripEntity> page = null;
            if(searchDto.getSrchCls().equals("mbti")){
                page =  tripService.findSearchTripMbtiPage(srchTripEntity, pageDto.getPageNum(), pageDto.getPerPage());
            } else if (searchDto.getSrchCls().equals("kwd")){
                page =  tripService.findSearchTripMbtiPage(srchTripEntity, pageDto.getPageNum(), pageDto.getPerPage());
            }
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            Message message = new Message();
            String resData = "";
            HashMap<String, Object> resMap = new HashMap<>();

            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            resMap.put("data", page.getContent());
            resMap.put("totalCnt", page.getTotalElements());
            resMap.put("totalPages", page.getTotalPages());
            resMap.put("currentPage", page.getNumber());

            resData = om.writeValueAsString(resMap);

            message.setStatus(HttpStatus.OK);
            message.setData(resData);
            message.setMessage("정상호출");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            Message message = new Message();
            message.setStatus(HttpStatus.BAD_GATEWAY);
            message.setMessage("호출중 오류 발생" + e);
            e.printStackTrace();
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }
       
    }
    @GetMapping(path="/rest/trip-search-multi")
    @ResponseBody
    public ResponseEntity tirpMultiSearchPage(HttpServletRequest request, SearchDto searchDto, PageDto pageDto) throws ParseException{
        try {
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            Message message = new Message();
            String resData = "";
            HashMap<String, Object> resMap = new HashMap<>();
            TripEntity srchTripEntity = new TripEntity();
            StringTokenizer st = null;
            if(searchDto.getSrchMbtia().length()>1){
                 st = new StringTokenizer(searchDto.getSrchMbtia(), "-");
            }else{
                //다중검색이기 때문에 예외처리
                message.setStatus(HttpStatus.OK);
                message.setData(resData);
                message.setMessage("mbti 수 부족");
                return new ResponseEntity<>(message, headers, HttpStatus.OK);
            }
            Page<TripEntity> page = null;
            List<String> mbtiaList = new ArrayList<>();
            
            mbtiaList.add(st.nextToken());
            mbtiaList.add(st.nextToken());
            page =  tripService.findSearchTripMbtiaMultiPage(mbtiaList, pageDto.getPageNum(), pageDto.getPerPage());

            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            resMap.put("data", page.getContent());
            resMap.put("totalCnt", page.getTotalElements());
            resMap.put("totalPages", page.getTotalPages());
            resMap.put("currentPage", page.getNumber());

            resData = om.writeValueAsString(resMap);

            message.setStatus(HttpStatus.OK);
            message.setData(resData);
            message.setMessage("정상호출");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            Message message = new Message();
            message.setStatus(HttpStatus.BAD_GATEWAY);
            message.setMessage("호출중 오류 발생" + e);
            e.printStackTrace();
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }
       
    }

    /**
     *  trip 등록
     * @param request
     * @param tripEntity
     */
    @PostMapping(path = "/rest/trip", consumes = "application/json")
    @ResponseBody
    public void tripCreate(HttpServletRequest request, @RequestBody TripEntity tripEntity){
        tripService.save(tripEntity);
    }

    @GetMapping(path = "/rest/trip/{_id}")
    @ResponseBody
    public ResponseEntity tripSelectOne(@PathVariable String _id) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        Message message = new Message();
        String resData = "";
        HashMap<String, Object> resMap = new HashMap<>();

        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Optional<TripEntity> base_trip = tripService.findOneById(_id);
        
        resMap.put("data", base_trip.get());

        resData = om.writeValueAsString(resMap);

        message.setStatus(HttpStatus.OK);
        message.setData(resData);
        message.setMessage("정상호출");
        
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @PutMapping(path = "/rest/trip/{_id}", consumes = "application/json")
    @ResponseBody
    public void tripUpdate(@RequestBody TripEntity tripEntity){
        Optional<TripEntity> base_trip = tripService.findOneById(tripEntity.get_Id());
        base_trip.get().setTripNm(tripEntity.getTripNm());
        base_trip.get().setTripCts(tripEntity.getTripCts());
        tripService.save(base_trip.get());
    }

    @DeleteMapping(path = "/rest/trip/{_id}")
    @ResponseBody
    public void tripDelete(@PathVariable String _id) throws JsonProcessingException{

        Optional<TripEntity> base_trip = tripService.findOneById(_id);
        
        tripService.delete(base_trip.get());
        
    }

    private String readJSONStringFromRequestBody(HttpServletRequest request){
        StringBuffer json = new StringBuffer();
        String line = null;
     
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
     
        }catch(Exception e) {
            System.out.println("Error reading JSON string: " + e.toString());
        }
        return json.toString();
    }
    
    
}
