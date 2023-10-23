package com.ashu.practice.service;

import com.ashu.practice.dto.JwtAuthenticationResponse;
import com.ashu.practice.dto.SignUpRequest;
import com.ashu.practice.dto.SigninRequest;

public interface AuthenticationService {
    void signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
