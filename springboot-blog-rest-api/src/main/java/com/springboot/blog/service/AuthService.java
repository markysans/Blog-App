package com.springboot.blog.service;

import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RefreshTokenDTO;
import com.springboot.blog.payload.RegisterDto;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    JWTAuthResponse refreshToken(RefreshTokenDTO refreshTokenDTO);
}
