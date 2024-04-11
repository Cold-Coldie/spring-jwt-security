package com.coldie.springsecurity.auth;

import com.coldie.springsecurity.config.JwtService;
import com.coldie.springsecurity.token.Token;
import com.coldie.springsecurity.token.TokenRepository;
import com.coldie.springsecurity.token.TokenType;
import com.coldie.springsecurity.user.Role;
import com.coldie.springsecurity.user.User;
import com.coldie.springsecurity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);

        return authResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, jwtToken);

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);

        return authResponse;
    }

    public void saveUserToken(User user, String jwtToken) {
        Token token = new Token();

        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        } else {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });

            tokenRepository.saveAll(validUserTokens);
        }
    }
}
