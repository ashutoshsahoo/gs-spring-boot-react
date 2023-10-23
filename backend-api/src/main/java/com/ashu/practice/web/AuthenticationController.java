package com.ashu.practice.web;


import com.ashu.practice.dto.JwtAuthenticationResponse;
import com.ashu.practice.dto.SignUpRequest;
import com.ashu.practice.dto.SigninRequest;
import com.ashu.practice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignUpRequest request) {
        authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}
