package com.muhaimintech.boilerplate.util;

import com.muhaimintech.boilerplate.userManagement.model.Permission;
import com.muhaimintech.boilerplate.userManagement.repository.PermissionRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CommonFunctions {

    private final PermissionRepository permissionRepository;

    public CommonFunctions(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // Helper to load permissions
    public Set<Permission> fetchPermissions(Set<Integer> permissionIds) {
        return new HashSet<>(permissionRepository.findAllById(permissionIds));
    }

}
