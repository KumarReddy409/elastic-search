package com.test.elasticsearch.controller;


import com.test.elasticsearch.dto.ElasticSearchHit;
import com.test.elasticsearch.service.SearchService;
import com.test.elasticsearch.utils.ValidateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kumar
 */

@RestController
@RequestMapping("/api/v1/elastic")
public class SearchController {


    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<ElasticSearchHit> getSearchData(@RequestParam(value = "latitude",required = false) Double latitude,
                                                @RequestParam(value = "longitude",required = false) Double longitude,
                                                @RequestParam(value = "searchText") String searchText,
                                                @RequestParam(value = "isExactMatch",defaultValue = "false") Boolean isExactMatch,
                                                @RequestParam(value = "from",defaultValue = "0") Integer from,
                                                @RequestParam(value = "size",defaultValue = "100") Integer size){
        ValidateRequest.validateSearchRequest(searchText);
        return this.searchService.getSearchData(latitude,longitude,searchText,isExactMatch,from,size);
    }

    @PostMapping("/{locationId}")
    public String uploadDocumentsToElasticSearch(@PathVariable(value = "locationId") String locationId){
      return this.searchService.uploadDocumentsByLocationId(locationId);
    }


    @PutMapping("/{itemIds}")
    public String updateDocumentsInElasticSearch(@PathVariable(value = "itemIds") List<String> itemIds){
        return this.searchService.updateDocumentsByItemIds(itemIds);
    }
}
