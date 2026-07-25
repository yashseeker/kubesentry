package com.yash.kubesentry.service;

import com.yash.kubesentry.dto.*;

public interface AuthService {
    void register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}