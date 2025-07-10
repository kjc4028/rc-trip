package com.search.srch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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
    public List<TripEntity> searchBySentence(@RequestBody SearchRequestDto request) throws IOException {
        return tripSearchService.searchBySentence(request.getSentence());
    }


}
