package com.test.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ElasticSearchResponse {

    @JsonProperty("hits")
    private ElasticSearchHits hit;
}
