package com.api.gateway.service;




import com.api.gateway.entity.User;
import com.api.gateway.exception.UserCreationException;
import com.api.gateway.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    // Return the list of roles directly from the user entity
    public List<String> getUserRoles(User user) {
        return user.getRoles();  // Assuming roles is a List<String>
    }


    public User createUser(String name, String email, String password, String rolesString) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Admin check logic remains same
        List<User> list = userRepository.findAll();
        for (User roles : list) {
            List<String> list1 = roles.getRoles();
            for (String role : list1) {
                if (rolesString.equalsIgnoreCase("admin")) {
                    throw new UserCreationException("Only one admin user is allowed.");
                }
            }
        }

        List<String> roles = Arrays.stream(rolesString.split(","))
                .map(role -> "ROLE_" + role.trim().toUpperCase())
                .toList();

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void sendOtpToEmail(String email, String otp) {
        WebClient.create("http://localhost:8085")
                .post()
                .uri("/email/send-otp")
                .bodyValue(Map.of("email", email, "otp", otp))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(); // fire-and-forget
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
