package com.ashu.practice.service.impl;

import com.ashu.practice.dto.JwtAuthenticationResponse;
import com.ashu.practice.dto.SignUpRequest;
import com.ashu.practice.dto.SigninRequest;
import com.ashu.practice.exception.UserAlreadyExistException;
import com.ashu.practice.model.Role;
import com.ashu.practice.model.User;
import com.ashu.practice.repositoty.UserRepository;
import com.ashu.practice.service.AuthenticationService;
import com.ashu.practice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public void signup(SignUpRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException(request.getEmail());
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(getRoles(request))
                .build();
        userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .accessToken(jwt)
                .build();
    }

    private List<Role> getRoles(SignUpRequest request) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        if (StringUtils.containsIgnoreCase(request.getFirstName(), "ADMIN")) {
            roles.add(Role.ADMIN);
            roles.add(Role.MODERATOR);
        } else if (StringUtils.containsIgnoreCase(request.getFirstName(), "MODERATOR")) {
            roles.add(Role.MODERATOR);
        }
        return roles;
    }
}
