package com.test.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchHits {


    @JsonProperty("hits")
    private List<ElasticSearchHit> hits;
}
