package com.abdullahturhan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleProductNotFoundEx(ProductNotFoundException exception){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("Message", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(QuantityOutOfRangeException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String,String> handleQuantityOutOfRangeEx(QuantityOutOfRangeException exception){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("Message", exception.getMessage());
        return errorMap;
    }


}
