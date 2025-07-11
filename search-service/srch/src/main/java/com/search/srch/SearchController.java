package com.search.srch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.search.common.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private TripEmbeddingService tripEmbeddingService;
    @Autowired
    private TripSearchService tripSearchService;
    @Autowired
    private TripElasticService tripElasticService;

    // 임베딩 전체 갱신 + 색인까지 한 번에
    @PostMapping("/index")
    public String embedAllTrips() throws IOException {
        tripEmbeddingService.embedAllTrips();
        tripElasticService.indexAllTrips();
        return "Embedding and indexing completed";
    }

    // 벡터 검색
    @PostMapping("/talk")
    public ResponseEntity<Message> searchBySentence(@RequestBody SearchRequestDto request) throws IOException {
        
        HttpHeaders headers = new HttpHeaders();
        Message message = new Message();
        
        if (request.getSentence() == null || request.getSentence().isEmpty()) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("검색어가 비어있습니다.");
            return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
        }
        
        try {  
            List<TripEntity> list = tripSearchService.searchBySentence(request.getSentence());
            if (list.isEmpty()) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("검색 결과가 없습니다.");
                return new ResponseEntity<>(message, headers, HttpStatus.NOT_FOUND);
            }
            message.setStatus(HttpStatus.OK);
            message.setData(list);
            message.setMessage("정상호출");
            
            return new ResponseEntity<>(message, headers, HttpStatus.OK);                    
        } catch (Exception e) {
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            message.setMessage("서버 오류: " + e.getMessage());
            return new ResponseEntity<>(message, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }


}
