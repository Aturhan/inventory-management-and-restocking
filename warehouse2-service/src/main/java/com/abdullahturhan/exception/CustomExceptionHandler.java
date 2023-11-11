package com.abdullahturhan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalidArgumentException(MethodArgumentNotValidException exception){
        Map<String,String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(err ->{
            errorMap.put(err.getField(),err.getDefaultMessage());
        });
        return errorMap;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public Map<String,String> handleProductNotFoundEx(ProductNotFoundException exception){
            Map<String,String> errorMap = new HashMap<>();
            errorMap.put("Message",exception.getMessage());
            return errorMap;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QuantityNotEnoughException.class)
    public Map<String,String> handleQuantityNotEnoughEx(QuantityNotEnoughException exception){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("Message", exception.getMessage());
        return errorMap;
    }
}
