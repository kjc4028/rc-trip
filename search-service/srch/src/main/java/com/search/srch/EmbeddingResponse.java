package com.search.srch;

import java.util.List;

public class EmbeddingResponse {
    private List<Double> embedding;

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }
}
