package com.search.srch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class TripElasticService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public void indexAllTrips() throws IOException {
        List<TripEntity> trips = mongoTemplate.findAll(TripEntity.class, "trip");
        System.out.println("indexAllTrips trips size " + trips);
        for (TripEntity trip : trips) {
            
            System.out.println("indexAllTrips trip " + trip.toString());
            
            IndexRequest<TripEntity> request = IndexRequest.of(i -> i
                .index("trip")
                .id(trip.get_Id())
                .document(trip)
            );


            IndexResponse response = elasticsearchClient.index(request);
        }
    }
} 