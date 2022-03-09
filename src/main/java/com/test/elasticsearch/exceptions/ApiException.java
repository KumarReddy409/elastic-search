package com.test.elasticsearch.exceptions;

import lombok.Data;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class ApiException {
    String message;
     HttpStatus httpStatus;
}
