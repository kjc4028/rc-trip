package com.trip.mbti.rest.trip;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mbti.rest.common.Message;

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

@RequestMapping("/rest/trip")
@RestController
public class TripController {
 
    @Autowired
    private TripService tripService;

    @GetMapping("/test")
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
    @GetMapping("")
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

    /**
     *  trip 등록
     * @param request
     * @param tripEntity
     */
    @PostMapping(path = "", consumes = "application/json")
    @ResponseBody
    public void tripCreate(HttpServletRequest request, @RequestBody TripEntity tripEntity){
        tripService.save(tripEntity);
    }

    @GetMapping(path = "/{_id}")
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

    @PutMapping(path = "/{_id}", consumes = "application/json")
    @ResponseBody
    public void tripUpdate(@RequestBody TripEntity tripEntity){
        Optional<TripEntity> base_trip = tripService.findOneById(tripEntity.get_Id());
        base_trip.get().setTripNm(tripEntity.getTripNm());
        base_trip.get().setTripCts(tripEntity.getTripCts());
        tripService.save(base_trip.get());
    }

    @DeleteMapping(path = "/{_id}")
    @ResponseBody
    public void tripDelete(@PathVariable String _id) throws JsonProcessingException{

        Optional<TripEntity> base_trip = tripService.findOneById(_id);
        
        tripService.delete(base_trip.get());
        
    }
}
