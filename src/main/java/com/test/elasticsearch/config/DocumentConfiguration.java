package com.test.elasticsearch.config;


import com.test.elasticsearch.dao.SearchDAO;
import com.test.elasticsearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;


/**
 * @author kumar
 */

@Configuration
@Slf4j
public class DocumentConfiguration {

    private final SearchService searchService;

    private final SearchDAO searchDAO;

    public DocumentConfiguration(SearchService searchService,
                                 SearchDAO searchDAO) {
        this.searchService = searchService;
        this.searchDAO = searchDAO;
    }

    /**
     *Initially at the time of project start time will load
     * fetch all data from database and load to elastic search
     */
    @PostConstruct
    public void loadDocuments(){
       this.searchService.uploadDocuments(searchDAO.getSearchData(null,new ArrayList<>()));
    }
}
