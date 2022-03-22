package com.test.elasticsearch.service;

import com.sun.xml.internal.bind.v2.TODO;
import com.test.elasticsearch.dao.SearchDAO;
import com.test.elasticsearch.utils.Constants;
import com.test.elasticsearch.dto.ElasticSearchHit;
import com.test.elasticsearch.dto.ElasticSearchResponse;
import com.test.elasticsearch.dto.SearchParameters;
import com.test.elasticsearch.exceptions.ServiceException;
import com.test.elasticsearch.index.SearchDocument;
import com.test.elasticsearch.index.TagDocument;
import com.test.elasticsearch.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Used to upload  and get search data from elastic search
 */
@Service
@Slf4j
public class SearchService {





    private final ElasticsearchOperations elasticsearchOperations;

    private final SearchDAO searchDAO;

    public SearchService(ElasticsearchOperations elasticsearchOperations,
                         SearchDAO searchDAO) {

        this.elasticsearchOperations = elasticsearchOperations;
        this.searchDAO = searchDAO;
    }

    /**
     * Used to upload documents to elastic search
     * @param searchParameters - structured data - Data from DB
     */
    public void uploadDocuments(List<SearchParameters> searchParameters) {
        List<SearchDocument> searchDocumentsList = this.formatStructuredDataToDocument(searchParameters);
        this.createIndexIfNotExists();
        IndexCoordinates indexCoordinates = IndexCoordinates.of(Constants.INDEX);
        List<IndexQuery> indexQueries = searchDocumentsList
                .stream()
                .map(searchDocument -> {
                    IndexQuery indexQuery = new IndexQuery();
                    indexQuery.setId(searchDocument.getId());
                    indexQuery.setSource(JsonUtils.JsonToString(searchDocument));
                    return indexQuery;
                })
                .collect(Collectors.toList());
        try {
            elasticsearchOperations.bulkIndex(indexQueries, indexCoordinates);
        }catch (Exception e){
            log.error("Exception occurred while inserting data to elastic search - {}",e.getMessage());
            throw new ServiceException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Used to create index if not exists
     */
    private void createIndexIfNotExists() {

        ClientResponse clientResponse;
        try {
            WebClient webClient = WebClient.builder().build();
            clientResponse = webClient
                    .head()
                    .uri("localhost:9200/" + Constants.INDEX + "?pretty")
                    .exchange()
                    .block();
            if (null != clientResponse && (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND) ||
            !clientResponse.statusCode().equals(HttpStatus.OK))) {
                 webClient
                        .put()
                        .uri("localhost:9200/" + Constants.INDEX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(this.getIndexSettings().toString()))
                        .exchange()
                        .block();
            }
        } catch (WebClientResponseException e){
            log.error("Exception Occurred While Creating Index - {}",e.getLocalizedMessage());
            throw new ServiceException(e.getLocalizedMessage(),e.getStatusCode());
        } catch (Exception e){
            log.error("Exception Occurred While Creating Index - {}",e.getLocalizedMessage());
            throw new ServiceException(e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Used to configure index settings like no of shards,no of replicas, refresh time
     * @return - JSONObject
     */
    private JSONObject getIndexSettings() {
        JSONObject settings = new JSONObject();
        JSONObject index = new JSONObject();
        JSONObject jsonData = new JSONObject();

        jsonData.put("number_of_shards", 2);
        jsonData.put("number_of_replicas", 1);
        //jsonData.put("refresh_interval", "2s");

        index.put("index", jsonData);
        settings.put("settings", index);
        return settings;
    }


    /**
     * Method performs search on provided search text in documents
     * latitude and longitude are used to get within specific radius like around 15km
     * @param latitude - latitude
     * @param longitude - longitude
     * @param searchText - text to be searched
     * @return - matching documents based on search text
     */
    public List<ElasticSearchHit> getSearchData(Double latitude,
                                                Double longitude,
                                                String searchText,
                                                Boolean isExactMatch,
                                                Integer from,
                                                Integer size){

        WebClient webClient = WebClient
                .builder()
                .build();
        String uri = "localhost:9200/_search?from=%s&size=%s";
        try {
            ElasticSearchResponse elasticSearchResponse = webClient
                    .post()
                    .uri(String.format(uri, from, size))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(this.getRequestBody(searchText,isExactMatch).toString()))
                    .retrieve()
                    .bodyToMono(ElasticSearchResponse.class)
                    .block();
            return elasticSearchResponse.getHit().getHits();
        } catch (Exception e){
           log.error("Exception occurred while getting search results from elastic search - {}",
                   e.getLocalizedMessage());
           // TODO: need to make sql search (backup case)
        }
        return null;
    }

    /**
     * Used to format request body for elastic search based on conditions
     * @param searchText - search text
     * @param isExactMatch - defines exact match needed
     * @return - JSONObject
     */
    private JSONObject getRequestBody(String searchText, Boolean isExactMatch) {
        String[] searchTextWords = searchText.split(" ");
        JSONObject jsonObject;
        if(isExactMatch){
           jsonObject = this.getQueryForElasticSearch(searchText, isExactMatch);
        }else if(searchTextWords.length == 1){
            jsonObject = this.getQueryForElasticSearch("*" + searchText + "*", isExactMatch);
        }else {
            jsonObject = this.getQueryForElasticSearch(this.constructSearchForMultipleWords(searchTextWords),
                    isExactMatch);
        }
        return jsonObject;
    }

    /**
     * Used to insert documents to elastic search by locationId
     * @param locationId - locationId
     * @return - message
     */
    public String uploadDocumentsByLocationId(String locationId) {
        if (StringUtils.isEmpty(locationId)) {
            log.error("Exception occurred while uploadDocumentsByLocationId - {}", "LocationId is missing");
            throw new ServiceException("LocationId is missing", HttpStatus.BAD_REQUEST);
        }
        this.uploadDocuments(this.searchDAO.getSearchData(locationId, new ArrayList<>()));
        return "Success";
    }
    /**
     * Used to update documents
     * @param itemIds - itemIds
     * @return - message
     */
    public String updateDocumentsByItemIds(List<String> itemIds){
        if(itemIds.isEmpty()){
            log.error("Exception occurred while uploadDocumentsByLocationId - {}","ItemIds are missing");
            throw new ServiceException("ItemIds are missing",HttpStatus.BAD_REQUEST);
        }
        this.uploadDocuments(this.searchDAO.getSearchData(null,itemIds));
        return "success";
    }

    /**
     * Used to format Structured data(Data from DB) to document
     * @param searchParameters - search parameters - structured data - Data from DB
     * @return - List<SearchDocument> -  list of documents
     */
    private List<SearchDocument> formatStructuredDataToDocument(List<SearchParameters> searchParameters){

        Map<String,SearchDocument> searchDocumentMap = new HashMap<>();
        if(!searchParameters.isEmpty()){
            searchParameters.forEach(searchParameter -> {
                String key = searchParameter.getItemId();
                if(searchDocumentMap.get(key) != null){
                    searchDocumentMap.get(key).getTagIndices().add(this.getTagDocument(searchParameter));
                }else {
                    SearchDocument searchDocument = this.getSearchDocument(searchParameter);
                    if(!StringUtils.isEmpty(searchParameter.getTagId()) &&
                            !StringUtils.isEmpty(searchParameter.getTagName())){
                        searchDocument.getTagIndices().add(this.getTagDocument(searchParameter));
                    }
                    searchDocumentMap.put(key,searchDocument);
                }
            });
        }
        return new ArrayList<>(searchDocumentMap.values());
    }

    /**
     * Used to map data from structured to searchDocument
     * @param searchParameter - structured data
     * @return - searchDocument
     */
    private SearchDocument getSearchDocument(SearchParameters searchParameter) {
        return SearchDocument
                .builder()
                .id(searchParameter.getLocationId() + Constants.UNDER_SCORE +
                        searchParameter.getItemId())
                .itemId(searchParameter.getItemId())
                .locationId(searchParameter.getLocationId())
                .itemImageId(searchParameter.getItemImageId())
                .itemImageType(searchParameter.getItemImageType())
                .city(searchParameter.getCity())
                .sellPrice(searchParameter.getSellPrice())
                .itemName(searchParameter.getItemName())
                .locationName(searchParameter.getLocationName())
                .tagIndices(new ArrayList<>())
                .build();
    }

    /**
     * Used to map data from structured to tagDocument
     * @param searchParameter - structured data
     * @return - tagDocument
     */
    private TagDocument getTagDocument(SearchParameters searchParameter) {
        return TagDocument
                .builder()
                .id(searchParameter.getTagId())
                .tagName(searchParameter.getTagName())
                .build();
    }

    /**
     * Constructing query for Search in elastic search
     * @param searchText - searchText
     * @param isExactMatch - to define query for exact match or not
     *
     */
    private JSONObject getQueryForElasticSearch(String searchText, boolean isExactMatch){
        long start = System.currentTimeMillis();
        JSONObject query = new JSONObject();
//        JSONObject termsQueryWithFilterField = new JSONObject();
//        JSONArray locationIdsForFilter = new JSONArray(locationIds);
//        termsQueryWithFilterField.put("locationId.keyword",locationIdsForFilter);
//        JSONArray filterQuery = new JSONArray();
//
//        JSONObject termsQuery = new JSONObject();
//        termsQuery.put("terms",termsQueryWithFilterField);
//        filterQuery.put(termsQuery);
        JSONObject boolQuery = new JSONObject();
        boolQuery.put("should",isExactMatch ? this.constructQueryForExactMatch(searchText) :
                this.constructQueryForPartialMatch(searchText));
        boolQuery.put( "minimum_should_match" ,1);
        //boolQuery.put("filter",filterQuery);
        JSONObject boolObject = new JSONObject();
        boolObject.put("bool",boolQuery);
        query.put("query",boolObject);
        return query;
    }

    /**
     * Used to construct query for ExactMatch
     */
    private JSONArray constructQueryForExactMatch(String searchText){
        JSONArray shouldQuery = new JSONArray();
        JSONObject matchQueryObject1 = new  JSONObject();
        JSONObject matchQueryObject2 = new  JSONObject();
        JSONObject matchQueryObject3 = new  JSONObject();
        matchQueryObject1.put("itemName.keyword",searchText);
        matchQueryObject2.put("tags.tagName.keyword",searchText);
        matchQueryObject3.put("locationName.keyword",searchText);
        JSONObject matchQuery1 = new  JSONObject();
        JSONObject matchQuery2 = new  JSONObject();
        JSONObject matchQuery3 = new  JSONObject();
        matchQuery1.put("match",matchQueryObject1);
        matchQuery2.put("match",matchQueryObject2);
        matchQuery3.put("match",matchQueryObject3);
        shouldQuery.put(matchQuery1);
        shouldQuery.put(matchQuery2);
        shouldQuery.put(matchQuery3);
        return shouldQuery;
    }

    /**
     * Used to construct query for Partial Match
     */
    private JSONArray constructQueryForPartialMatch(String searchText){
        JSONArray shouldQuery = new JSONArray();
        JSONArray fields = new JSONArray();
        fields.put("itemName");
        fields.put("locationName");
        fields.put("tags.tagName");
        JSONObject queryStringFields = new JSONObject();
        queryStringFields.put( "query",searchText);
        queryStringFields.put("fields",fields);
        JSONObject queryString = new JSONObject();
        queryString.put( "query_string",queryStringFields);
        shouldQuery.put(queryString);
        return shouldQuery;
    }

    /**
     * Constructing search text for accurate results
     * @param searchText - search text words
     * @return - search text
     */
    private String constructSearchForMultipleWords(String[] searchText) {
        StringBuilder textToSearch = new StringBuilder();
        for (int i =0; i< searchText.length;i++){
            if(i == searchText.length-1){
                textToSearch.append("*").append(searchText[i]).append("*");
            }else {
                textToSearch.append(searchText[i]).append(" ").append("AND").append(" ");
            }
        }
        return textToSearch.toString();
    }
}
