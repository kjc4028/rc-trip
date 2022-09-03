package com.trip.mbti.rest.trip;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mbti.rest.common.MbtiCommUtil;
import com.trip.mbti.rest.common.Message;
import com.trip.mbti.rest.common.PageDto;
import com.trip.mbti.rest.common.SearchDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripController {
 
    @Autowired
    private TripService tripService;

    private final UserServiceClient userServiceClient;

    @GetMapping("/trips/test")
    @ResponseBody
    public ResponseEntity<Message> tirpAllList(TripEntity tripentity){
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
    @GetMapping(path="/trips")
    @ResponseBody
    public ResponseEntity<Message> tirpAllPage(HttpServletRequest request, TripEntity tripentity){
        try {
            int pageNum = 1;
            int perPage = 10;
            if(request.getParameter("pageNum") != null && !request.getParameter("pageNum").equals("anObject")){
                pageNum = Integer.parseInt(request.getParameter("pageNum"));
            }
            
            if(request.getParameter("perPage") != null && !request.getParameter("perPage").equals("anObject")){
                perPage = Integer.parseInt(request.getParameter("perPage"));
            }

            Page<TripEntity> page =  tripService.findAllTripPage(pageNum, perPage);
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            Message message = new Message();
            HashMap<String, Object> resMap = new HashMap<>();

            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            resMap = om.convertValue(page, HashMap.class);

            message.setStatus(HttpStatus.OK);
            message.setData(resMap);
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

    @GetMapping(path="/trips/searching/base")
    @ResponseBody
    public ResponseEntity<Message> tirpSearchPage(HttpServletRequest request, SearchDto searchDto, PageDto pageDto) throws ParseException{
        try {
            TripEntity srchTripEntity = searchDto.toEntity();
            Page<TripEntity> page = null;
            if(searchDto.getSrchCls().equals("mbti")){
                page =  tripService.findSearchTripMbtiPage(srchTripEntity, pageDto.getPageNum(), pageDto.getPerPage());
            } else if (searchDto.getSrchCls().equals("kwd")){
                page =  tripService.findSearchTripMbtiPage(srchTripEntity, pageDto.getPageNum(), pageDto.getPerPage());
            }
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            Message message = new Message();
            HashMap<String, Object> resMap = new HashMap<>();

            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            resMap = om.convertValue(page, HashMap.class);

            message.setStatus(HttpStatus.OK);
            message.setData(resMap);
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
    @GetMapping(path="/trips/searching/multi")
    @ResponseBody
    public ResponseEntity<Message> tirpMultiSearchPage(HttpServletRequest request, SearchDto searchDto, PageDto pageDto) throws ParseException{
        try {
            ObjectMapper om = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            Message message = new Message();
            HashMap<String, Object> resMap = new HashMap<>();
            
            StringTokenizer st_A = null;
            StringTokenizer st_B = null;
            StringTokenizer st_C = null;
            StringTokenizer st_D = null;
            
            Page<TripEntity> page = null;
            
            Set<String> mbtiaSet = new HashSet<>();
            Set<String> mbtibSet = new HashSet<>();
            Set<String> mbticSet = new HashSet<>();
            Set<String> mbtidSet = new HashSet<>();

            st_A = MbtiCommUtil.StringTokenizerMbti(searchDto.getSrchMbtia(), ",");
            st_B = MbtiCommUtil.StringTokenizerMbti(searchDto.getSrchMbtib(), ",");
            st_C = MbtiCommUtil.StringTokenizerMbti(searchDto.getSrchMbtic(), ",");
            st_D = MbtiCommUtil.StringTokenizerMbti(searchDto.getSrchMbtid(), ",");
            
            MbtiCommUtil.StringTokenizerAddList(st_A, mbtiaSet);
            MbtiCommUtil.StringTokenizerAddList(st_B, mbtibSet);
            MbtiCommUtil.StringTokenizerAddList(st_C, mbticSet);
            MbtiCommUtil.StringTokenizerAddList(st_D, mbtidSet);

            page =  tripService.findSearchTripMbtiMultiPage(mbtiaSet, mbtibSet, mbticSet, mbtidSet, pageDto.getPageNum(), pageDto.getPerPage());

            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            resMap = om.convertValue(page, HashMap.class);

            message.setStatus(HttpStatus.OK);
            message.setData(resMap);
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
    @PostMapping(path = "/trips", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public void tripCreate(HttpServletRequest request,TripRequestDto tripRequestDto){
        TripEntity tripEntity = tripRequestDto.toEntity();
        tripService.save(tripEntity);
    }

    /**
     *  trip 등록
     * @param request
     * @param tripEntity
     */
    @PostMapping(path = "/trips", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Message> tripCreateJson(HttpServletRequest request, @RequestBody TripRequestDto tripRequestDto){
        System.out.println("claimtest=============");
        System.out.println(request.getHeader("Authorization"));
        System.out.println(userServiceClient.getClaim(request.getHeader("Authorization"), "userId"));
        ResponseEntity<Message> resUserId = userServiceClient.getClaim(request.getHeader("Authorization"), "userId");
        TripEntity tripEntity = tripRequestDto.toEntity();
        tripEntity.setRegUserId(resUserId.getBody().getData().toString());
        tripService.save(tripEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/trips/{_id}")
    @ResponseBody
    public ResponseEntity<Message> tripSelectOne(@PathVariable String _id) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        Message message = new Message();
        HashMap<String, Object> resMap = new HashMap<>();

        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Optional<TripEntity> base_trip = tripService.findOneById(_id);
        
        resMap = om.convertValue(base_trip.get(), HashMap.class);

        message.setStatus(HttpStatus.OK);
        message.setData(resMap);
        message.setMessage("정상호출");
        
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @PutMapping(path = "/trips/{_id}", consumes = "application/json")
    @ResponseBody
    public void tripUpdate(@RequestBody TripRequestDto tripRequestDto){
        
        Optional<TripEntity> base_trip = tripService.findOneById(tripRequestDto.get_Id());
        base_trip.get().setTripNm(tripRequestDto.getTripNm());
        base_trip.get().setTripCts(tripRequestDto.getTripCts());
        tripService.save(base_trip.get());
    }

    @DeleteMapping(path = "/trips/{_id}")
    @ResponseBody
    public void tripDelete(@PathVariable String _id) throws JsonProcessingException{

        Optional<TripEntity> base_trip = tripService.findOneById(_id);
        
        tripService.delete(base_trip.get());
        
    }

    @DeleteMapping(path="/trips/user")
    @ResponseBody
    public void tripDeleteBuRegUserId(@RequestBody TripRequestDto tripRequestDto){
        TripEntity tripEntity = tripRequestDto.toEntity();
        tripService.deleteByRegUserId(tripEntity.getRegUserId());
    }
    
    
}
