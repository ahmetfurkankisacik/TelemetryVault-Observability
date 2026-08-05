package com.telemetryvault.auth.service;

import com.telemetryvault.auth.dto.*;
import com.telemetryvault.auth.entity.Role;
import com.telemetryvault.auth.entity.User;
import com.telemetryvault.auth.repository.UserRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Tracer tracer;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       Tracer tracer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tracer = tracer;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        Span span = tracer.nextSpan().name("user-registration-db").tag("user.email", request.getEmail()).start();
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            log.info("Processing User Registration for email: {}", request.getEmail());

            if (userRepository.existsByEmail(request.getEmail())) {
                log.warn("Registration failed: Email [{}] already exists", request.getEmail());
                throw new IllegalArgumentException("Email already in use");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(Role.ROLE_USER);
            user.setActive(true);

            User savedUser = userRepository.save(user);
            log.info("User registered successfully with ID: {}", savedUser.getId());

            String accessToken = jwtService.generateAccessToken(savedUser);
            String refreshToken = jwtService.generateRefreshToken(savedUser);

            UserDto userDto = new UserDto(savedUser.getId(), savedUser.getEmail(),
                    savedUser.getFirstName(), savedUser.getLastName(), savedUser.getRole(), savedUser.isActive());

            return new AuthResponse(accessToken, refreshToken, jwtService.getJwtExpiration() / 1000, userDto);
        } finally {
            span.end();
        }
    }

    public AuthResponse login(LoginRequest request) {
        Span span = tracer.nextSpan().name("user-login-auth").tag("user.email", request.getEmail()).start();
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            log.info("Processing User Login for email: {}", request.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            log.info("User authenticated successfully: ID={}", user.getId());

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            UserDto userDto = new UserDto(user.getId(), user.getEmail(),
                    user.getFirstName(), user.getLastName(), user.getRole(), user.isActive());

            return new AuthResponse(accessToken, refreshToken, jwtService.getJwtExpiration() / 1000, userDto);
        } finally {
            span.end();
        }
    }
}
