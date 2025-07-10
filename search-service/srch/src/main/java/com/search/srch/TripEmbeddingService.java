package com.search.srch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TripEmbeddingService {
    @Autowired
    private MongoTemplate mongoTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${embedurl}")
    private String embedUrl;

    public void embedAllTrips() {
        List<TripEntity> trips = mongoTemplate.findAll(TripEntity.class, "trip");
        System.out.println("trips size >>> " + trips.size());
        for (TripEntity trip : trips) {
            System.out.println("trip str >>> " + trip.toString());
            Map<String, String> request = new HashMap<>();
            request.put("sentence", trip.getTripCts());
            EmbeddingResponse response = restTemplate.postForObject(
                embedUrl,
                request,
                EmbeddingResponse.class
            );
            System.out.println("embedAllTrips response " + response.toString());
            List<Double> embedding = response != null ? response.getEmbedding() : null;
            trip.setEmbeddingScore(embedding);
            mongoTemplate.save(trip, "trip");
        }
    }

    public static class EmbeddingResponse {
        private List<Double> embedding;
        public List<Double> getEmbedding() { return embedding; }
        public void setEmbedding(List<Double> embedding) { this.embedding = embedding; }
    }
} 