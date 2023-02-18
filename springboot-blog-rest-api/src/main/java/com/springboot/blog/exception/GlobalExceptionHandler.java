package com.springboot.blog.exception;

import com.springboot.blog.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<Object> handleBlogAPIException(BlogAPIException blogAPIException, WebRequest webRequest) {
        return ResponseEntity.status(blogAPIException.getStatus()).body(
                ErrorDetails.builder()
                        .timestamp(LocalDate.now())
                        .message(blogAPIException.getMessage())
                        .details(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleBlogAPIException(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorDetails.builder()
                        .timestamp(LocalDate.now())
                        .message(resourceNotFoundException.getMessage())
                        .details(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorDetails.builder()
                                .timestamp(LocalDate.now())
                                .message(exception.getMessage())
                                .details(webRequest.getDescription(false))
                                .build()
                );
    }
}
