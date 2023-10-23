package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.time.Instant;

public class UserAlreadyExistException extends ErrorResponseException {
    public UserAlreadyExistException(String email) {
        super(HttpStatus.BAD_REQUEST,
                asProblemDetail("User with email=" + email + " already exists"),
                null);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle("User with Email already exists");
        problemDetail.setType(URI.create("https://api.users.com/errors/already-exists"));
        problemDetail.setProperty("errorCategory", "Already Exists");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
