package com.test.elasticsearch.index;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;

@Builder
@Data
public class TagDocument {

    @Id
    private String id;

    private String tagName;
}
