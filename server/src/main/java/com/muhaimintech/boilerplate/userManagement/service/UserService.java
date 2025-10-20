package com.muhaimintech.boilerplate.userManagement.service;

import com.muhaimintech.boilerplate.userManagement.dto.ChangePasswordDto;
import com.muhaimintech.boilerplate.userManagement.dto.UserRequestAPDto;
import com.muhaimintech.boilerplate.userManagement.dto.UserRequestRBACDto;
import com.muhaimintech.boilerplate.userManagement.model.AccessProfile;
import com.muhaimintech.boilerplate.userManagement.model.Role;
import com.muhaimintech.boilerplate.userManagement.model.User;
import com.muhaimintech.boilerplate.userManagement.repository.AccessProfileRepository;
import com.muhaimintech.boilerplate.userManagement.repository.RoleRepository;
import com.muhaimintech.boilerplate.userManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccessProfileRepository accessProfileRepository;

    // ===== Common =====

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        userRepository.deleteById(id);
    }

    // ===== Role-Based =====

    public User createRoleBasedUser(UserRequestRBACDto request) {
        validateUniqueEmailAndUsername(request.getEmail(), request.getUsername());

        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one role is required for role-based users.");
        }
        if (request.getRoleIds().size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only one role is allowed per user in RBAC setup.");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        if (roles.size() != request.getRoleIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role ID(s).");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setName(request.getName());
        user.setUserType(request.getUserType());
        user.setRoles(roles);
        user.setAccesses(Collections.emptySet());
        user.setDeleted(false);

        return userRepository.save(user);
    }

    public User updateRoleBasedUser(Integer id, UserRequestRBACDto request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        validateEmailAndUsernameForUpdate(request.getEmail(), request.getUsername(), id);

        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one role is required for role-based users.");
        }
        if (request.getRoleIds().size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only one role is allowed per user in RBAC setup.");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        if (roles.size() != request.getRoleIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role ID(s).");
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setUsername(request.getUsername());
        existing.setPhone(request.getPhone());
        existing.setRoles(roles);
        existing.setAccesses(Collections.emptySet());
        existing.setUserType(request.getUserType());

        return userRepository.save(existing);
    }

    // ===== Access-Based =====

    public User createAccessBasedUser(UserRequestAPDto request) {
        validateUniqueEmailAndUsername(request.getEmail(), request.getUsername());

        if (request.getAccessIds() == null || request.getAccessIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one access profile is required for access-based users.");
        }

        Set<AccessProfile> accesses = new HashSet<>(accessProfileRepository.findAllById(request.getAccessIds()));
        if (accesses.size() != request.getAccessIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid access profile ID(s).");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setName(request.getName());
        user.setUserType(request.getUserType());
        user.setRoles(Collections.emptySet());
        user.setAccesses(accesses);
        user.setDeleted(false);

        return userRepository.save(user);
    }

    public User updateAccessBasedUser(Integer id, UserRequestAPDto request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        validateEmailAndUsernameForUpdate(request.getEmail(), request.getUsername(), id);

        if (request.getAccessIds() == null || request.getAccessIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one access profile is required for access-based users.");
        }

        Set<AccessProfile> accesses = new HashSet<>(accessProfileRepository.findAllById(request.getAccessIds()));
        if (accesses.size() != request.getAccessIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid access profile ID(s).");
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setUsername(request.getUsername());
        existing.setPhone(request.getPhone());
        existing.setRoles(Collections.emptySet());
        existing.setAccesses(accesses);
        existing.setUserType(request.getUserType());

        return userRepository.save(existing);
    }

    public void changePassword(String username, ChangePasswordDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current Password Is Incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ===== Validation Helpers =====

    private void validateUniqueEmailAndUsername(String email, String username) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
        }
    }

    private void validateEmailAndUsernameForUpdate(String email, String username, Integer userId) {
        userRepository.findByEmail(email)
                .filter(u -> !u.getId().equals(userId))
                .ifPresent(u -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use."); });

        userRepository.findByUsername(username)
                .filter(u -> !u.getId().equals(userId))
                .ifPresent(u -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use."); });
    }
}