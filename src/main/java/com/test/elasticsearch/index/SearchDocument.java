package com.test.elasticsearch.index;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.List;

@Builder
@Data
@Document(indexName = "magilhub_search",shards = 2,replicas = 1,refreshInterval = "10s")
public class SearchDocument {

    @Id
    private String id;

    private String locationId;

    private String locationName;

    private String city;

    private String itemId;

    private String itemName;

    private String sellPrice;

    private String itemImageId;

    private String itemImageType;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<TagDocument> tagIndices;
}
