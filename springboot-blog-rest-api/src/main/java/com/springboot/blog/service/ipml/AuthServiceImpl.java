package com.springboot.blog.service.ipml;

import com.springboot.blog.entity.RefreshToken;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RefreshTokenDTO;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.repository.RefreshTokenRepository;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public JWTAuthResponse login(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserNameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userName = authentication.getName();
        String token = jwtTokenProvider.generateTokenByUserName(userName);
        User user = userRepository.findByUserNameOrEmail(userName, userName).orElseThrow();
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        return JWTAuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public String register(RegisterDto registerDto) {
        // check if username exists in database
        if(Boolean.TRUE.equals(userRepository.existsByUserName(registerDto.getUserName()))) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "User Name Already Exist");
        }
        // check if email exists in database
        if(Boolean.TRUE.equals(userRepository.existsByEmail(registerDto.getEmail()))) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists in database!");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUserName(registerDto.getUserName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        roleSet.add(userRole);
        user.setRoles(roleSet);

        userRepository.save(user);
        return "User Registered Successfully!";

    }



    @Override
    public JWTAuthResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {
        return refreshTokenRepository.findByToken(refreshTokenDTO.getRefreshToken())
                .map(jwtTokenProvider::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenByUserName(user.getUserName());
                    return JWTAuthResponse.builder()
                            .accessToken(token)
                            .refreshToken(refreshTokenDTO.getRefreshToken())
                            .tokenType("Bearer")
                            .build();

                })
                .orElseThrow(() -> new BlogAPIException(HttpStatus.BAD_REQUEST, "Refresh Token Not Found"));

    }

}
