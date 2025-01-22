package io.research.userservice.controller;

import io.research.userservice.repository.entity.User;
import io.research.userservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.register(User.builder()
                        .isActive(true)
                        .password(user.getPassword())
                        .username(user.getUsername())
                .build()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/isAdmin")
    public boolean isUserAdmin(@PathVariable Long userId) {
        // Логіка для перевірки, чи є користувач адміністратором
        return userService.isAdmin(userId);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        @NonNull
        private String username;
        @org.springframework.lang.NonNull
        private String password;
    }
}
