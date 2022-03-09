package com.test.elasticsearch.service;

import com.test.elasticsearch.dto.SearchParameters;
import com.test.elasticsearch.index.SearchDocument;

import java.util.ArrayList;
import java.util.List;

public class SearchServiceHelper {

    public static List<SearchParameters> getSearchParameters(){
        List<SearchParameters> searchParameters = new ArrayList<>();
        searchParameters.add(getSearchParameters1());
        searchParameters.add(getSearchParameters2());
        return searchParameters;
    }


    private static SearchParameters getSearchParameters1(){
       return SearchParameters
                .builder()
                .itemId("686a90f2-2cf9-11eb-89bf-0242ac170002")
                .itemName("French Fries Pizza")
                .itemImageId("09b8bcf5-034a-11eb-89bf-0242ac170002")
                .itemImageType("img/PNG")
                .locationId("11")
                .tagId("1")
                .tagName("southindian")
                .build();
    }
    private static SearchParameters getSearchParameters2(){
        return SearchParameters
                .builder()
                .itemId("686a90f2-2cf9-11eb-89bf-0242ac170002")
                .itemName("French Fries Pizza")
                .itemImageId("09b8bcf5-034a-11eb-89bf-0242ac170002")
                .itemImageType("img/PNG")
                .locationId("11")
                .tagId("2")
                .tagName("northindian")
                .build();
    }

    public static String getSearchData(){
        return "{\n" +
                "  \"took\": 10,\n" +
                "  \"timed_out\": false,\n" +
                "  \"_shards\": {\n" +
                "    \"total\": 1,\n" +
                "    \"successful\": 1,\n" +
                "    \"skipped\": 0,\n" +
                "    \"failed\": 0\n" +
                "  },\n" +
                "  \"hits\": {\n" +
                "    \"total\": {\n" +
                "      \"value\": 1,\n" +
                "      \"relation\": \"eq\"\n" +
                "    },\n" +
                "    \"max_score\": 1.0,\n" +
                "    \"hits\": [\n" +
                "      {\n" +
                "        \"_index\": \"magilhub_search\",\n" +
                "        \"_type\": \"_doc\",\n" +
                "        \"_id\": \"11_686b8411-2cf9-11eb-89bf-0242ac170002\",\n" +
                "        \"_score\": 1.0,\n" +
                "        \"_source\": {\n" +
                "          \"id\": \"11_686b8411-2cf9-11eb-89bf-0242ac170002\",\n" +
                "          \"locationId\": \"11\",\n" +
                "          \"locationName\": \"Ever Fresh, Madurai\",\n" +
                "          \"itemId\": \"686b8411-2cf9-11eb-89bf-0242ac170002\",\n" +
                "          \"itemName\": \"Schezwan Chicken\",\n" +
                "          \"itemImageId\": null,\n" +
                "          \"itemImageType\": null,\n" +
                "          \"tagIndices\": [\n" +
                "            {\n" +
                "              \"id\": \"1\",\n" +
                "              \"tagName\": \"southindian\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": \"2\",\n" +
                "              \"tagName\": \"northindian\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }
}
