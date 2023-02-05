package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<Object> handleBlogAPIException(BlogAPIException blogAPIException) {
        return ResponseEntity.status(blogAPIException.getStatus()).body(blogAPIException.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleBlogAPIException(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("%s not found with %s: '%s' ", resourceNotFoundException.getResourceName(),
                resourceNotFoundException.getFieldName(), resourceNotFoundException.getFieldValue()));
    }
}
