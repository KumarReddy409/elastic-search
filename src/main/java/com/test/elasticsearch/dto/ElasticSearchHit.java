package com.test.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticSearchHit {

    @JsonProperty("_source")
    private Object _source;
}
