package com.springboot.blog.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class RegisterDto {
    private String name;
    private String userName;
    private String email;
    private String password;
}
