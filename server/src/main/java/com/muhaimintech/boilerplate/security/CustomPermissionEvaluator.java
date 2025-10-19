package com.muhaimintech.boilerplate.security;

import com.muhaimintech.boilerplate.userManagement.model.User;
import com.muhaimintech.boilerplate.userManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object accessType, Object permission) {
        if (authentication != null && accessType instanceof String) {
            String type = String.valueOf(accessType);
            String perm = String.valueOf(permission);
            Object principal = authentication.getPrincipal();

            User user = null;

            // check if principal is custom User
            if (principal instanceof User u) {
                user = u;
            }
            // check if principal is Spring Security UserDetails
            else if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
                // fetch your User entity from DB
                user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            }

            if (user != null) {
                // Admin bypass
                if ("ADMIN".equalsIgnoreCase(user.getUserType()) || "SUPER_ADMIN".equalsIgnoreCase(user.getUserType())) {
                    return true;
                }

                if ("ACCESS".equalsIgnoreCase(type)) {
                    return user.getAccesses().stream()
                            .anyMatch(a -> a.getName().equalsIgnoreCase(perm));
                }

                if ("RBAC".equalsIgnoreCase(type)) {
                    return user.getRoles().stream()
                            .flatMap(role -> role.getPermissions().stream())
                            .anyMatch(p -> p.getName().equalsIgnoreCase(perm));
                }
            }
        }
        return false;
    }


    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}