package io.research.userservice.controller;

import io.research.userservice.repository.entity.User;
import io.research.userservice.service.UserService;
import io.research.userservice.util.JwtUtil;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // Authentication Endpoints
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserDTO user) {
        try {
            // Register the user
            User registeredUser = userService.register(
                    User.builder()
                            .isActive(true)
                            .password(user.getPassword()) // Ensure the password is encoded
                            .username(user.getUsername())
                            .role("ADMIN")
                            .build()
            );

            // Authenticate the user after registration
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // Generate a JWT token for the authenticated user
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // Return the response with the token
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Authentication failed after registration");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during registration");
        }
    }

    // User Management Endpoints
    @GetMapping("/auth/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/auth")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/auth/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/auth/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/{userId}/isAdmin")
    public ResponseEntity<Boolean> isUserAdmin(@PathVariable Long userId) {
        boolean isAdmin = userService.isAdmin(userId);
        return ResponseEntity.ok(isAdmin);
    }

    // DTOs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        @NonNull
        private String username;
        @org.springframework.lang.NonNull
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthRequest {
        private String username;
        private String password;
    }

    @Value
    @Builder
    public static class AuthResponse {
        String token;
    }
}
