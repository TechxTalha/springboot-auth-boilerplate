package com.muhaimintech.boilerplate.userManagement.controller;

import com.muhaimintech.boilerplate.userManagement.dto.RoleRequest;
import com.muhaimintech.boilerplate.userManagement.model.Permission;
import com.muhaimintech.boilerplate.userManagement.model.Role;
import com.muhaimintech.boilerplate.userManagement.repository.RoleRepository;
import com.muhaimintech.boilerplate.util.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CommonFunctions commonFunctions;


    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Integer id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        Set<Permission> permissions = commonFunctions.fetchPermissions(roleRequest.getPermissionIds());
        if (permissions.size() != roleRequest.getPermissionIds().size()) {
            return ResponseEntity.badRequest().body("One or more permission IDs are invalid.");
        }

        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setPermissions(permissions);

        Role saved = roleRepository.save(role);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        return roleRepository.findById(id)
                .map(existing -> {
                    Set<Permission> permissions = commonFunctions.fetchPermissions(roleRequest.getPermissionIds());
                    if (permissions.size() != roleRequest.getPermissionIds().size()) {
                        return ResponseEntity.badRequest().body("One or more permission IDs are invalid.");
                    }
                    existing.setName(roleRequest.getName());
                    existing.setPermissions(permissions);
                    Role updated = roleRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



}