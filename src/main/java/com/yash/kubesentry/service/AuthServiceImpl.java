package com.yash.kubesentry.service;

import com.yash.kubesentry.dto.*;
import com.yash.kubesentry.model.entity.User;
import com.yash.kubesentry.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.yash.kubesentry.repository.*;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequestDTO request){
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        String encodedPassword =
                passwordEncoder.encode(request.getPassword());
        User user = User.builder().username(request.getUsername()).email(request.getEmail()).password(encodedPassword).role(request.getRole()).build();
        userRepository.save(user);
    }


    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getIdentifier(),request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return AuthResponseDTO.builder().token(token).build();
    }
}