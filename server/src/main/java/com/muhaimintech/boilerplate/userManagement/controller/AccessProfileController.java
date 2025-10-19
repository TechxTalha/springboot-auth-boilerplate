package com.muhaimintech.boilerplate.userManagement.controller;

import com.muhaimintech.boilerplate.userManagement.model.AccessProfile;
import com.muhaimintech.boilerplate.userManagement.repository.AccessProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/access-profile")
public class AccessProfileController {

    @Autowired
    private AccessProfileRepository accessProfileRepository;

    @GetMapping
    public ResponseEntity<List<AccessProfile>> getAllAccesses() {
        List<AccessProfile> accesses = accessProfileRepository.findAll();
        return ResponseEntity.ok(accesses);
    }


    @PostMapping
    public ResponseEntity<?> createAccessProfile(@RequestBody AccessProfile accessProfile) {
        Optional<AccessProfile> existing = accessProfileRepository.findByName(accessProfile.getName());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Access profile with this name already exists.");
        }

        AccessProfile saved = accessProfileRepository.save(accessProfile);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccessProfile(@PathVariable Integer id, @RequestBody AccessProfile updatedAccessProfile) {
        Optional<AccessProfile> existingProfile = accessProfileRepository.findById(id);
        if (existingProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<AccessProfile> existingByName = accessProfileRepository.findByName(updatedAccessProfile.getName());
        if (existingByName.isPresent() && !existingByName.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body("Another access profile with this name already exists.");
        }

        AccessProfile accessProfile = existingProfile.get();
        accessProfile.setName(updatedAccessProfile.getName());
        AccessProfile saved = accessProfileRepository.save(accessProfile);

        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccessProfile(@PathVariable Integer id) {
        Optional<AccessProfile> accessProfile = accessProfileRepository.findById(id);
        if (accessProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        accessProfileRepository.deleteById(id);
        return ResponseEntity.ok("Access profile deleted successfully.");
    }

}
