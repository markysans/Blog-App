package com.springboot.blog.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
public class JWTAuthResponse {
    private String accessToken;
    private String tokenType;
}
