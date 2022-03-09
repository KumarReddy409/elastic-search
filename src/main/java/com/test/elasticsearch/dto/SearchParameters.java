package com.test.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchParameters {

    private String locationId;

    private String locationName;

    private String city;

    private String itemId;

    private String itemName;

    private String sellPrice;

    private String itemImageId;

    private String itemImageType;

    private String tagId;

    private String tagName;

}
