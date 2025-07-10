package com.search.srch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.json.JsonData;

@Service
public class TripSearchService {
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${embedurl}")
    private String embedUrl;

    public List<TripEntity> searchBySentence(String sentence) throws IOException {
        Map<String, String> request = new HashMap<>();
        request.put("sentence", sentence);
        EmbeddingResponse response = restTemplate.postForObject(
            embedUrl,
            request,
            EmbeddingResponse.class
        );
        List<Double> embedding = response != null ? response.getEmbedding() : null;
        String script = "cosineSimilarity(params.query_vector, 'embeddingScore') + 1.0";
        Map<String, JsonData> params = Map.of("query_vector", JsonData.of(embedding));
        Query query = Query.of(q -> q
            .scriptScore(ScriptScoreQuery.of(sq -> sq
                .query(Query.of(q2 -> q2.matchAll(ma -> ma)))
                .script(Script.of(s -> s
                    .source(script)
                    .lang("painless")
                    .params(params)
                ))
            ))
        );
        SearchRequest searchRequest = SearchRequest.of(s -> s
            .index("trip")
            .query(query)
            .size(10)
            .source(SourceConfig.of(src -> src.filter(f -> f.includes("id", "tripCts", "embeddingScore"))))
        );
        SearchResponse<TripEntity> responseEs = elasticsearchClient.search(searchRequest, TripEntity.class);
        return responseEs.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public static class EmbeddingResponse {
        private List<Double> embedding;
        public List<Double> getEmbedding() { return embedding; }
        public void setEmbedding(List<Double> embedding) { this.embedding = embedding; }
    }
} 