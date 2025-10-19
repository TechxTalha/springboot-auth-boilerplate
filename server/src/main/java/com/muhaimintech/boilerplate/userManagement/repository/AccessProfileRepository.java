package com.muhaimintech.boilerplate.userManagement.repository;

import com.muhaimintech.boilerplate.userManagement.model.AccessProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessProfileRepository extends JpaRepository<AccessProfile,Integer> {
    Optional<AccessProfile> findByName(String name);
}
