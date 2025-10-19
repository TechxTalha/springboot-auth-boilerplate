package com.muhaimintech.boilerplate.userManagement.controller;

import com.muhaimintech.boilerplate.userManagement.dto.UserRequestAPDto;
import com.muhaimintech.boilerplate.userManagement.dto.UserRequestRBACDto;
import com.muhaimintech.boilerplate.userManagement.model.User;
import com.muhaimintech.boilerplate.userManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    // ========== Common APIs ==========

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            return userService.getUserById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, "User not found."));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);

            return ResponseEntity.ok("User Deleted Successfully");
        } catch (ResponseStatusException ex) {
            return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // ========== Role-Based APIs ==========

    @PostMapping("/rbac")
    public ResponseEntity<?> createRoleBasedUser(@RequestBody UserRequestRBACDto request) {
        try {
            User user = userService.createRoleBasedUser(request);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException ex) {
            return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/rbac/{id}")
    public ResponseEntity<?> updateRoleBasedUser(@PathVariable Integer id, @RequestBody UserRequestRBACDto request) {
        try {
            User user = userService.updateRoleBasedUser(id, request);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException ex) {
            return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // ========== Access-Based APIs ==========

    @PostMapping("/access")
    public ResponseEntity<?> createAccessBasedUser(@RequestBody UserRequestAPDto request) {
        try {
            User user = userService.createAccessBasedUser(request);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException ex) {
            return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/access/{id}")
    public ResponseEntity<?> updateAccessBasedUser(@PathVariable Integer id, @RequestBody UserRequestAPDto request) {
        try {
            User user = userService.updateAccessBasedUser(id, request);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException ex) {
            return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
//
//    @PreAuthorize("hasPermission('ACCESS', 'Test')")
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint(Authentication auth) {
        return ResponseEntity.ok(auth.getPrincipal().toString());
    }

}
