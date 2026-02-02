package com.api.gateway.controller;

import com.api.gateway.dto.RegisterRequest;
import com.api.gateway.entity.OtpToken;
import com.api.gateway.entity.User;
import com.api.gateway.repository.OtpTokenRepository;
import com.api.gateway.security.JwtUtil;
import com.api.gateway.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OtpTokenRepository otpTokenRepository;

    public AuthController(JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder,OtpTokenRepository otpTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.otpTokenRepository = otpTokenRepository;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        return Mono.fromCallable(() -> userService.findByEmail(email))
                .flatMap(optionalUser -> {
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        if (passwordEncoder.matches(password, user.getPassword())) {
                            System.out.println("User name: " + user.getName());
                            String token = jwtUtil.generateToken(email, userService.getUserRoles(user),user.getName());
                            return Mono.just(ResponseEntity.ok(Map.of("token", token)));
                        }
                    }
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<User>> register(@Valid @RequestBody RegisterRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();
        String roles = "USER"; // ✅ Always default to USER

        return Mono.fromCallable(() -> userService.findByEmail(email))
                .flatMap(optionalUser -> {
                    if (optionalUser.isPresent()) {
                        return Mono.just(ResponseEntity.status(400).<User>body(null));
                    }
                    String cleanedPassword = password.trim();
                    return Mono.fromCallable(() -> userService.createUser(name, email, cleanedPassword, roles))
                            .map(createdUser -> ResponseEntity.ok(createdUser));
                });
    }

    @PostMapping("/register-receptionist")
    public Mono<ResponseEntity<User>> registerReceptionist(@Valid @RequestBody RegisterRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();
        String roles = "RECEPTIONIST"; // 👈 Assign receptionist role

        return Mono.fromCallable(() -> userService.findByEmail(email))
                .flatMap(optionalUser -> {
                    if (optionalUser.isPresent()) {
                        return Mono.just(ResponseEntity.status(400).<User>body(null));
                    }
                    String cleanedPassword = password.trim();
                    return Mono.fromCallable(() -> userService.createUser(name, email, cleanedPassword, roles))
                            .map(createdUser -> ResponseEntity.ok(createdUser));
                });
    }


    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        userService.sendOtpToEmail(email, otp);

        // optionally store OTP in DB or cache here

        OtpToken token = new OtpToken(email, otp, LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(token);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        Optional<OtpToken> tokenOpt = otpTokenRepository.findByEmailAndOtp(email, otp);
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        }

        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user); // ✅ Add save method in UserService if not present

        return ResponseEntity.ok("Password reset successful");
    }
}
