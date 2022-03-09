package com.test.elasticsearch.exceptions;


import org.elasticsearch.search.SearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class SearchExceptionHandler {


    @ExceptionHandler(value = SearchRequestException.class)
    public ResponseEntity<Object> handleSearchRequestException(SearchRequestException searchRequestException){
        HttpStatus httpStatus = searchRequestException.getHttpStatus();
      ApiException apiException = new ApiException(searchRequestException.getMessage(),httpStatus);
      return new ResponseEntity<>(apiException,httpStatus);
    }

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<Object> handleElasticSearchException(ServiceException serviceException){
        HttpStatus httpStatus = serviceException.getHttpStatus();
        ApiException apiException = new ApiException(serviceException.getMessage(),httpStatus);
        return new ResponseEntity<>(apiException,httpStatus);
    }

}
