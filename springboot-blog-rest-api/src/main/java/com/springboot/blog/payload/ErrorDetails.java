package com.springboot.blog.payload;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class ErrorDetails {
    private LocalDate timestamp;
    private String message;
    private String details;
}
