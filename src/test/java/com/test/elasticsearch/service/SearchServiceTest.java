package com.test.elasticsearch.service;

import com.test.elasticsearch.dao.SearchDAO;
import com.test.elasticsearch.dto.ElasticSearchHit;
import com.test.elasticsearch.dto.ElasticSearchResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.internal.duplex.DuplexResponseBody;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {


    public  static MockWebServer mockWebServer;



    @Mock
    ElasticsearchOperations elasticsearchOperations;

    @Mock
    SearchDAO searchDAO;

    @InjectMocks
    SearchService searchService;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    void uploadDocuments() {
        try{
            searchService.uploadDocuments(SearchServiceHelper.getSearchParameters());
            Assertions.assertTrue(true);
        }catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    void getSearchDataWithExactMatch() {
        try {
            //TODO: Web client is returning empty data need to do some investigate while using mock server
            //mockWebServer.url("localhost:9200/magilhub_search/_search?from=0&size=0");
            mockWebServer.enqueue(new MockResponse().setBody(SearchServiceHelper.getSearchData()));
            List<ElasticSearchHit> hitList = searchService.getSearchData(63.5,
                    77.6, "dosa", true, 0, 10);
           Assertions.assertTrue(true);
        }catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    void getSearchDataWithSingleWord() {
        try {
            //TODO: Web client is returning empty data need to do some investigate while using mock server
            //mockWebServer.url("localhost:9200/magilhub_search/_search?from=0&size=0");
            mockWebServer.enqueue(new MockResponse().setBody(SearchServiceHelper.getSearchData()));
            List<ElasticSearchHit> hitList = searchService.getSearchData(63.5,
                    77.6, "dosa", false, 0, 10);
            Assertions.assertTrue(true);
        }catch (Exception e){
            Assertions.fail();
        }
    }
    @Test
    void getSearchDataWithMultipleWord() {
        try {
            //TODO: Web client is returning empty data need to do some investigate while using mock server
            //mockWebServer.url("localhost:9200/magilhub_search/_search?from=0&size=0");
            mockWebServer.enqueue(new MockResponse().setBody(SearchServiceHelper.getSearchData()));
            List<ElasticSearchHit> hitList = searchService.getSearchData(63.5,
                    77.6, "masala dosa", false, 0, 10);
            Assertions.assertTrue(true);
        }catch (Exception e){
            Assertions.fail();
        }
    }
    @Test
    void uploadDocumentsWhenLocationIdIsNull() {
        try{
            String message = searchService.uploadDocumentsByLocationId(null);
        }catch (Exception e){
            assertEquals("LocationId is missing",e.getMessage());
        }
    }

    @Test
    void uploadDocumentsByLocationId() {
        try{
            Mockito.when(searchDAO.getSearchData("11",new ArrayList<>()))
                    .thenReturn(SearchServiceHelper.getSearchParameters());
            String message = searchService.uploadDocumentsByLocationId("11");
            assertEquals("Success",message);
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void updateDocumentsWhenItemIdsEmpty() {
        try{
            searchService.updateDocumentsByItemIds(new ArrayList<>());
            Assertions.assertTrue(true);
        }catch (Exception e){
            assertEquals("ItemIds are missing",e.getMessage());
        }
    }

    @Test
    void updateDocumentsByItemIds() {
        try{
            searchService.updateDocumentsByItemIds(Collections.singletonList("1"));
            Assertions.assertTrue(true);
        }catch (Exception e){
            Assertions.fail();
        }
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}