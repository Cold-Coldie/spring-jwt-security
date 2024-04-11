package com.coldie.springsecurity.auth;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Operation(
            description = "Register/Create a new user.",
            summary = "This endpoint is used to register a new user to the database and you get a JWT token in return to progress."

    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticationService.register(request), HttpStatus.OK);
    }

    @Operation(
            description = "Login/Sign in an existing user.",
            summary = "This endpoint is used to sign in an existing user and get a JWT token in return to progress."

    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticationService.authenticate(request), HttpStatus.OK);
    }
}
