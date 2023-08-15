package com.springboot.blog.exception;

import com.springboot.blog.payload.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException, WebRequest webRequest) {

        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult()
                .getAllErrors().forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);

//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                ErrorDetails.builder()
//                        .timestamp(LocalDate.now())
//                        .message(resourceNotFoundException.getMessage())
//                        .details(webRequest.getDescription(false))
//                        .build()
//        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorDetails.builder()
                        .timestamp(LocalDate.now())
                        .message(accessDeniedException.getMessage())
                        .details(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {
        log.info(exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorDetails.builder()
                                .timestamp(LocalDate.now())
                                .message(exception.getMessage())
                                .details(webRequest.getDescription(true))
                                .build()
                );
    }
}
