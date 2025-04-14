package com.operis.project.adapter.in.rest.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError (Integer statusCode, String HttpStatus, String message, List<String> details){

    public ApiError {
        if (statusCode == null) {
            throw new IllegalArgumentException("statusCode cannot be null");
        }
        if (HttpStatus == null) {
            throw new IllegalArgumentException("HttpStatus cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
    }

    public ApiError(Integer statusCode, String HttpStatus, String message) {
        this(statusCode, HttpStatus, message, null);
    }



}
