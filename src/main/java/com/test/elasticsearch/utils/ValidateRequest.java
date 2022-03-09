package com.test.elasticsearch.utils;

import com.test.elasticsearch.exceptions.SearchRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * @author kumar
 * Used to validate requests
 */
@Slf4j
public class ValidateRequest {

    public static void validateSearchRequest(String searchText){
        if(StringUtils.isEmpty(searchText)){
            log.error("Exception occurred while validating request - {}","searchText parameter are required");
            throw new SearchRequestException("searchText parameter are required", HttpStatus.BAD_REQUEST);
        }
    }
}
